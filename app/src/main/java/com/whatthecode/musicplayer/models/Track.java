package com.whatthecode.musicplayer.models;

/**
 * Created by Nipun Haldar on 6/8/2017.
 */

public class Track {

    public final long id;
    public final String title;
    public final String artistName;
    public final String albumName;
    public final int duration;
    public final int trackNumber;
    public final long albumId;
    public final long artistId;

    public Track(long _id, String _title, String _artistName, String _albumName, int _duration, int _trackNumber, long _albumId, long _artistId) {
        this.id = _id;
        this.title = _title;
        this.artistName = _artistName;
        this.albumName = _albumName;
        this.duration = _duration;
        this.trackNumber = _trackNumber;
        this.albumId = _albumId;
        this.artistId = _artistId;
    }
}
