import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'playlists',
        loadChildren: () => import('./playlist/playlist.module').then(m => m.ApolloPlaylistModule)
      },
      {
        path: 'tracks',
        loadChildren: () => import('./track/track.module').then(m => m.TrackModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ApolloEntityModule {}
