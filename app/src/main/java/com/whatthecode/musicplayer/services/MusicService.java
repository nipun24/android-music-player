package com.whatthecode.musicplayer.services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import com.whatthecode.musicplayer.models.Track;

import java.util.ArrayList;

/**
 * Created by faztp on 09-Jun-17.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    // Media Player
    private MediaPlayer player;
    // Track List
    private ArrayList<Track> tracks;
    // track position
    private int trackPosition;

    private final IBinder serviceBinder = new MusicBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        initMusicPlayer();
    }

    public void initMusicPlayer() {
        // create new instance & set player properties
        player = new MediaPlayer();

        // WAKELOCK permission is required to use this method
        // Keeps the phone awake partially to allow the playback
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        // Is this an ALARM ? or a ringtone ? or is this a Music ?
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }


    public void playSong(){
        // play the current song
        // stop any currently playing song
        player.reset();

        // get track id
        long track_id = tracks.get(trackPosition).id;
        // get uri
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, track_id
        );

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
    }

    public void setTrackList(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // when prepared, start track
        mp.start();
    }


    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
