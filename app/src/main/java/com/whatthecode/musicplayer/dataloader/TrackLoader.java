package com.whatthecode.musicplayer.dataloader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.whatthecode.musicplayer.models.Track;

import java.util.ArrayList;

/**
 * Created by faztp on 09-Jun-17.
 */

public class TrackLoader {


    /**
     * Returns a list of Tracks for a given Cursor
     * @param cursor Cursor, output of a content resolver query
     * @return Returns a list of Tracks
     */
    public static ArrayList<Track> getTracksForCursor(Cursor cursor) {

        // make an array list of type Track to hold the tracks
        ArrayList<Track> arrayList = new ArrayList<>();

        // check if cursor is not null and check if cursor is empty
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // we fetch the columns in the following order.
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String artist = cursor.getString(2);
                String album = cursor.getString(3);
                int duration = cursor.getInt(4);
                int trackNumber = cursor.getInt(5);
                long artistId = cursor.getInt(6);
                long albumId = cursor.getLong(7);

                arrayList.add(new Track(id, title, artist, album, duration, trackNumber, artistId, albumId));
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();

        return arrayList;
    }


    /**
     * Returns a cursor object after query
     * TODO: Add Selection
     * @param context
     * @return
     */
    public static Cursor getTrackCursor(Context context) {
        String selection_statement = "is_music=1 AND title!=''";
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id"}, selection_statement, null, "title");
    }


    /**
     * Returns all tracks on device
     * @return
     */
    public static ArrayList<Track> getAllTracks(Context context) {
        return getTracksForCursor(getTrackCursor(context));
    }
}
