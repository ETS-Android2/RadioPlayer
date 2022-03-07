package com.kanika.radioplayerapp.Ui.Adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kanika.radioplayerapp.Model.AlbumDetails;
import com.kanika.radioplayerapp.R;
import com.kanika.radioplayerapp.databinding.SongsItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */

//Display song item in recycleview
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder> {


    Context context;
    List<AlbumDetails> albumList ;

    public SongListAdapter(Context context) {
        this.context = context;
        albumList = new ArrayList<>();
    }


    public void clear() {
        this.albumList.clear();
    }

    public void addAll(List<AlbumDetails> songList) {

        try {
            this.albumList.clear();
            this.albumList.addAll(songList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SongListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(SongsItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongListAdapter.MyViewHolder holder, int position) {
        if(albumList!=null && albumList.size()>0) {
            AlbumDetails albumDetail = albumList.get(position);
            Glide.with(context)
                    .load(albumDetail.getImageUrl())
                    .placeholder(R.mipmap.bg_default)
                    .error(R.mipmap.bg_default)
                    .into(holder.binding.imgAlbumpic);
            if(!TextUtils.isEmpty(albumDetail.getArtist()))
                holder.binding.textArtistname.setText(albumDetail.getArtist());
            if(!TextUtils.isEmpty(albumDetail.getName()))
                holder.binding.textAlbumname.setText(albumDetail.getName());
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private SongsItemBinding binding;

        private MyViewHolder(SongsItemBinding songsItemBinding) {
            super(songsItemBinding.getRoot());
            this.binding = songsItemBinding;
        }
    }

}
