package com.kanika.radioplayerapp.Network;
import com.kanika.radioplayerapp.Model.AlbumDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */
public interface Api {

    @GET("nowplaying/v3/935/testapi")
    Call<List<AlbumDetails>>getSongList();
}
