package com.kanika.radioplayerapp.Network;

import android.content.Context;
import android.widget.Toast;

import com.kanika.radioplayerapp.Model.AlbumDetails;
import com.kanika.radioplayerapp.Repository.AlbumRespository;
import com.kanika.radioplayerapp.Utils.CommonUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */
public class ApiController {
    //get songlist from api

    public void getSongListRequest(Context mContext, AlbumRespository albumRespository) {
        if (CommonUtils.isInternetConnected(mContext)) {
            Retrofit retrofit = new Retrofit();
            Call<List<AlbumDetails>> call = retrofit.api.getSongList();
            call.enqueue(new Callback<List<AlbumDetails>>() {
                @Override
                public void onResponse(Call<List<AlbumDetails>> call, Response<List<AlbumDetails>> response) {
                    if (response.isSuccessful()) {
                        albumRespository.insert(response.body());

                    }
                }
                @Override
                public void onFailure(Call<List<AlbumDetails>> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(mContext,"No internet connection",Toast.LENGTH_SHORT).show();
        }
    }
}
