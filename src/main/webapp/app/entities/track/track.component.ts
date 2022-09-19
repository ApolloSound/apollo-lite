import {Component, OnInit} from '@angular/core';
import {IUser} from "app/core/user/user.model";
import {HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {ITrack, Track} from 'app/shared/model/track.model';
import {FormBuilder} from "@angular/forms";
import {TrackService} from "app/shared/services/track.service";
import {JhiAlertService} from "ng-jhipster";

@Component({
  selector: 'sf-track',
  templateUrl: './track.component.html',
  styleUrls: ['./track.component.scss']
})
export class TrackComponent implements OnInit {

  saveForm = this.fb.group({
    id: [],
    name: [],
    rating: [],
    url: [],
    popularity: [],
    thumbnail: [],
    createdAt: [],
    duration: [],
    primaryColor: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected trackService: TrackService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    console.error('init');
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    const rawSong = this.saveForm.get('upload')!.value;
    this.uploadCloudinary(rawSong).then(url => { // USE CLOUDINARY SERVICE TO UPLOAD THE SONG
      const track = this.createFromForm();
      track.url = url;
      return track;
    }).then(track => {
      this.subscribeToSaveResponse(this.trackService.create(track));
    }).catch(err => {
      console.error(err);
    });
  }

  private uploadCloudinary(rawSong: string | Blob): Promise<string> {
    const formData = new FormData();
    formData.append('file', rawSong);
    formData.append('upload_preset', 'sftest');
    return fetch('https://api.cloudinary.com/v1_1/dzjxhjx1i/image/upload', {
      method: 'POST',
      body: formData
    })
      .then(res => res.json())
      .then(res => res.secure_url);
  }

  private createFromForm(): ITrack {
    return {
      ...new Track(),
      name: this.saveForm.get(['name'])!.value,
      duration: this.saveForm.get(['duration'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrack>>): void {
    result.subscribe(() => this.onSaveSuccess());
  }

  protected onSaveSuccess(): void {
    console.error('saved');
  }

  protected onError(errorMessage: string): void {
    this.jhiAlertService.error(errorMessage, null);
  }

  trackUserById(index: number, item: IUser): any {
    return item.id;
  }
}
