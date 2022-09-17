package com.apollo.web.rest.errors;

import com.apollo.service.exception.UserNotFoundException;
import com.apollo.service.exception.UsernameAlreadyUsedException;
import io.github.jhipster.web.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.apollo.web.rest.errors.ErrorConstants.*;
import static org.zalando.problem.Status.*;


/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String VIOLATIONS_KEY = "violations";
    private static final String CODE_KEY = "code";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed.
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return entity;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
            .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
            .withStatus(problem.getStatus())
            .withTitle(problem.getTitle())
            .with(PATH_KEY, request.getNativeRequest(HttpServletRequest.class).getRequestURI());

        if (problem instanceof ConstraintViolationProblem) {
            builder
                .with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations())
                .with(MESSAGE_KEY, ERR_VALIDATION);
        } else {
            builder
                .withCause(((DefaultProblem) problem).getCause())
                .withDetail(problem.getDetail())
                .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey(MESSAGE_KEY) && problem.getStatus() != null) {
                builder.with(MESSAGE_KEY, "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
            .map(f -> new FieldErrorVM(f.getObjectName().replaceFirst("DTO$", ""), f.getField(), f.getCode()))
            .collect(Collectors.toList());

        Problem problem = Problem.builder()
            .withType(CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Method argument not valid")
            .withStatus(defaultConstraintViolationStatus())
            .with(MESSAGE_KEY, ERR_VALIDATION)
            .with(FIELD_ERRORS_KEY, fieldErrors)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEmailAlreadyUsedException(com.apollo.service.exception.EmailAlreadyUsedException ex, NativeWebRequest request) {
        EmailAlreadyUsedException problem = new EmailAlreadyUsedException();
        return create(problem, request, HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUsernameAlreadyUsedException(UsernameAlreadyUsedException ex, NativeWebRequest request) {
        LoginAlreadyUsedException problem = new LoginAlreadyUsedException();
        return create(problem, request, HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleInvalidPasswordException(com.apollo.service.exception.InvalidPasswordException ex, NativeWebRequest request) {
        return create(new InvalidPasswordException(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(applicationName, true, ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(CONFLICT)
            .with(MESSAGE_KEY, ERR_CONCURRENCY_FAILURE)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUserNotFoundException(UserNotFoundException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withType(NOT_FOUND_TYPE)
            .withStatus(NOT_FOUND)
            .with(MESSAGE_KEY, ex.getMessage())
            .with(CODE_KEY, ERR_USER_NOT_FOUND)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadFollowerException(IllegalArgumentException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withType(DEFAULT_TYPE)
            .withStatus(BAD_REQUEST)
            .with(MESSAGE_KEY, ex.getMessage())
            .with(CODE_KEY, ERR_GENERIC)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleExternalClientException(com.apollo.service.exception.ExternalClientError ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withType(DEFAULT_TYPE)
            .withStatus(SERVICE_UNAVAILABLE)
            .with(MESSAGE_KEY, ex.getMessage())
            .with(CODE_KEY, ERR_SERVICE_UNAVAILABLE)
            .build();
        return create(ex, problem, request);
    }
}
