import { ITrack } from 'app/shared/model/track.model';

export interface IAlbum {
  id?: number;
  title?: string;
  year?: number;
  thumbnail?: string;
  totalTracks?: number;
  userLogin?: string;
  userId?: number;
  tracks?: ITrack[];
}

export class Album implements IAlbum {
  constructor(
    public id?: number,
    public title?: string,
    public year?: number,
    public thumbnail?: string,
    public totalTracks?: number,
    public userLogin?: string,
    public userId?: number,
    public tracks?: ITrack[]
  ) {}
}
