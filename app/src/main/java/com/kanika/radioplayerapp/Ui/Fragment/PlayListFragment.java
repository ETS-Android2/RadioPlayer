package com.kanika.radioplayerapp.Ui.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.kanika.radioplayerapp.Model.AlbumDetails;
import com.kanika.radioplayerapp.Network.ApiController;
import com.kanika.radioplayerapp.R;
import com.kanika.radioplayerapp.Repository.AlbumRespository;
import com.kanika.radioplayerapp.Ui.Adapter.SongListAdapter;
import com.kanika.radioplayerapp.ViewModel.AlbumViewModal;
import com.kanika.radioplayerapp.databinding.FragmentPlayListBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */
public class PlayListFragment extends Fragment {

    FragmentPlayListBinding binding;
    Activity activity;
    private AlbumViewModal albumViewModal;
    private AlbumRespository albumRespository;

    public PlayListFragment() {
        // Required empty public constructor
    }


    public static PlayListFragment newInstance() {
        PlayListFragment fragment = new PlayListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_list, container, false);
        View view = binding.getRoot();
        if (activity == null) {
            activity = getActivity();
        }
        init();
        return view;

    }

    private void init() {
        //create adapter object and set layout manager
        SongListAdapter songListAdapter = new SongListAdapter(activity);
        binding.rvSongList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvSongList.setLayoutManager(linearLayoutManager);
        binding.rvSongList.setAdapter(songListAdapter);
        //get album repository instance using singletone.
        albumRespository = new AlbumRespository(activity.getApplication());
        //create  viewmodel instance
        albumViewModal = new ViewModelProvider(this).get(AlbumViewModal.class);
        //call getsonglist api and get songslist from network
        ApiController apiController = new ApiController();
        apiController.getSongListRequest(activity,albumRespository);
        albumViewModal.getAllAlbum().observe(getActivity(), new Observer<List<AlbumDetails>>() {
            @Override
            public void onChanged(List<AlbumDetails> albumList) {
                if (albumList != null && albumList.size() > 0) {
                    //display songlist in recycleview
                    songListAdapter.addAll(albumList);
                }
            }
        });

    }
}