import {Routes} from '@angular/router';

import {UserRouteAccessService} from 'app/core/auth/user-route-access-service';
import {TrackComponent} from "app/entities/track/track.component";

export const trackRoutes: Routes = [
  {
    path: '',
    redirectTo: 'new'
  },
  {
    path: 'new',
    component: TrackComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ApolloApp.playlist.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
];
