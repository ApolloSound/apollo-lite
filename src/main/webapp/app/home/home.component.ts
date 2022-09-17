import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { IPlaylist } from 'app/shared/model/playlist.model';
import { HttpResponse } from '@angular/common/http';
import { PlaylistService } from 'app/shared/services/playlist.service';

@Component({
  selector: 'sf-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  playlists: IPlaylist[] = [];

  constructor(private playlistService: PlaylistService, private accountService: AccountService) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.playlistService.query().subscribe(
      (res: HttpResponse<IPlaylist[]>) => {
        this.playlists = res.body ? res.body : [];
      },
      () => {
        console.error('Error getting the playlists');
      }
    );
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
