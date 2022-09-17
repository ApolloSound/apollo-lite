import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgJhipsterModule } from 'ng-jhipster';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { ContextMenuModule } from 'ngx-contextmenu';
import { TrackBaseComponent } from 'app/shared/component/track-base.component';

@NgModule({
  imports: [ContextMenuModule.forRoot({ useBootstrap4: true })],
  exports: [
    FormsModule,
    CommonModule,
    NgbModule,
    NgJhipsterModule,
    InfiniteScrollModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    TranslateModule,
    CarouselModule,
    ContextMenuModule
  ],
  declarations: [TrackBaseComponent]
})
export class ApolloSharedLibsModule {}
