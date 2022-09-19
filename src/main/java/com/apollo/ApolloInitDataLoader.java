package com.apollo;

import com.apollo.domain.Authority;
import com.apollo.repository.AuthorityRepository;
import com.apollo.security.AuthoritiesConstants;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.apollo.domain.Authority.fromName;

@Component
public class ApolloInitDataLoader implements ApplicationRunner {

    private AuthorityRepository authorityRepository;

    public ApolloInitDataLoader(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!this.authorityRepository.findAll().isEmpty()) {
            return;
        }

        this.authorityRepository.saveAndFlush(fromName(AuthoritiesConstants.ADMIN));
        this.authorityRepository.saveAndFlush(fromName(AuthoritiesConstants.USER));
    }
}
