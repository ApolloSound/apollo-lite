import {NgModule} from '@angular/core';
import {TrackComponent} from './track.component';
import {ApolloSharedModule} from "app/shared/shared.module";
import {RouterModule} from "@angular/router";
import {trackRoutes} from "app/entities/track/track.route";


@NgModule({
  declarations: [TrackComponent],
  imports: [
    ApolloSharedModule, RouterModule.forChild(trackRoutes)
  ]
})
export class TrackModule { }
