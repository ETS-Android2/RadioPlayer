package com.kanika.radioplayerapp.Network;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */
//Make Retrofilt client instance.
public class Retrofit {
    retrofit2.Retrofit retrofit=new retrofit2.Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public Api api=retrofit.create(Api.class);

}
