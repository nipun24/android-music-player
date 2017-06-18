package com.whatthecode.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.whatthecode.musicplayer.models.Track;
import com.whatthecode.musicplayer.services.MusicService;

import java.util.ArrayList;

/**
 * Created by faztp on 18-Jun-17.
 */

public class MusicPlayer {

    // Music Service variables
    private static MusicService musicService;
    private static Intent musicIntent;
    private static boolean serviceBound = false;
    private static Context context;
    private static ArrayList<Track> currentPlayList;

    /**
     * Initialize the music player with service
     * @param _context
     */
    public static void Init(Context _context) {
        context = _context;
        if (musicIntent == null) {
            musicIntent = new Intent(context, MusicService.class);
            context.bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE);
            context.startService(musicIntent);
        }
    }

    /**
     * Call this method to update
     * @param tracks
     */
    public static void setServiceTrackList(ArrayList<Track> tracks) {
        if (tracks == null)
            tracks = new ArrayList<>();

        currentPlayList = tracks;
        ArrayList<Long> ids = new ArrayList<>();
        for (Track t : tracks) {
            ids.add(t.id);
        }
        if (serviceBound)
            musicService.setTrackList(ids);
    }

    /**
     * Play!
     */
    public static void playTrackAtIndex(int index) {
        musicService.setTrackPosition(index);
        musicService.playTrack();
    }

    private static ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            // get service
            musicService = binder.getService();
            serviceBound = true;
            setServiceTrackList(currentPlayList);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
}
