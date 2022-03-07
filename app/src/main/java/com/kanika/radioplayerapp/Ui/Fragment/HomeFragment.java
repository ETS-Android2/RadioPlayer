package com.kanika.radioplayerapp.Ui.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.kanika.radioplayerapp.Model.AlbumDetails;
import com.kanika.radioplayerapp.Network.ApiController;
import com.kanika.radioplayerapp.Network.Retrofit;
import com.kanika.radioplayerapp.R;
import com.kanika.radioplayerapp.Repository.AlbumRespository;
import com.kanika.radioplayerapp.ViewModel.AlbumViewModal;
import com.kanika.radioplayerapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */

//display current play song
public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    Activity activity;
    private AlbumViewModal albumViewModal;
    private List<AlbumDetails> SongList;
    private AlbumRespository albumRespository;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();
        if (activity == null) {
            activity = getActivity();
        }
        init();
        return view;
    }

    private void init() {
        //get album repository instance using singletone.
        albumRespository=new AlbumRespository(activity.getApplication());
        //create song list instance
        SongList=new ArrayList<>();
        //create  viewmodel instance
        albumViewModal=new ViewModelProvider(this).get(AlbumViewModal.class);
        //call getsonglist api and get songslist from network
        ApiController apiController = new ApiController();
        apiController.getSongListRequest(activity,albumRespository);
        albumViewModal.getAllAlbum().observe(getActivity(), new Observer<List<AlbumDetails>>() {
            @Override
            public void onChanged(List<AlbumDetails> albumList) {
                //get songlist using livedata anf viewmodel
                if(albumList!=null && albumList.size()>0) {
                    //get current first song from list
                    AlbumDetails albumDetail = albumList.get(0);
                    //display image from first album object
                    Glide.with(activity)
                            .load(albumDetail.getImageUrl())
                            .placeholder(R.mipmap.bg_default)
                            .error(R.mipmap.bg_default)
                            .into(binding.imgAlbum);
                    //display artist name
                    if(!TextUtils.isEmpty(albumDetail.getArtist()))
                    binding.textArtistName.setText(albumDetail.getArtist());
                    //display songname
                    if(!TextUtils.isEmpty(albumDetail.getName()))
                    binding.textSongTitle.setText(albumDetail.getName());

                }
            }
        });

    }

}