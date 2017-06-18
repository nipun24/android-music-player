package com.whatthecode.musicplayer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatthecode.musicplayer.MusicPlayer;
import com.whatthecode.musicplayer.R;
import com.whatthecode.musicplayer.models.Track;

import java.util.ArrayList;


public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ItemHolder> {

    private Context context;
    private ArrayList<Track> tracks;

    public TrackAdapter(Context _context, ArrayList<Track> _tracks) {
        this.context = _context;
        this.tracks = _tracks;
        MusicPlayer.setServiceTrackList(_tracks);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
            ItemHolder ml = new ItemHolder(v);
            return ml;
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int i) {
        Track localItem = tracks.get(i);

        itemHolder.title.setText(localItem.title);
        itemHolder.artist.setText(localItem.artistName);
    }

    @Override
    public int getItemCount() {
        return tracks != null ? tracks.size() : 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView title, artist;
        protected ImageView albumArt;

        public ItemHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.track_item_title);
            this.artist = (TextView) itemView.findViewById(R.id.track_item_artist);
            this.albumArt = (ImageView) itemView.findViewById(R.id.track_item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MusicPlayer.playTrackAtIndex(getAdapterPosition());
        }
    }
}
