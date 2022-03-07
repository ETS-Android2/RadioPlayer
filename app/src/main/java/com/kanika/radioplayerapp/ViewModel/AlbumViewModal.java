package com.kanika.radioplayerapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.kanika.radioplayerapp.Model.AlbumDetails;
import com.kanika.radioplayerapp.Repository.AlbumRespository;

import java.util.List;
/**
 * Created by Kanika Tandel on 07/03/2022.
 */
public class AlbumViewModal extends AndroidViewModel {

    private AlbumRespository albumRespository;
    private LiveData<List<AlbumDetails>> getAllAlbum;

    public AlbumViewModal(@NonNull Application application) {
        super(application);
        albumRespository=new AlbumRespository(application);
        getAllAlbum=albumRespository.getAllAlbum();
    }

    public void insert(List<AlbumDetails> list)
    {
        albumRespository.insert(list);
    }

    public LiveData<List<AlbumDetails>> getAllAlbum()
    {
        return getAllAlbum;
    }

}
