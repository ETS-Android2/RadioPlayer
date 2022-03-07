package com.kanika.radioplayerapp.Repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.kanika.radioplayerapp.Dao.AlbumDao;
import com.kanika.radioplayerapp.Database.AlbumDatabase;
import com.kanika.radioplayerapp.Model.AlbumDetails;
import java.util.List;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */
public class AlbumRespository {
    private AlbumDatabase database;
    private LiveData<List<AlbumDetails>> getAllAlbum;

    public AlbumRespository(Application application)
    {
        database=AlbumDatabase.getInstance(application);
        getAllAlbum=database.albumDao().getAllAlbumlist();
    }

    public void insert(List<AlbumDetails> songList){
     new InsertAsynTask(database).execute(songList);
    }
    public void delete(){
        new DeleteAsynTask(database).execute();
    }

    public LiveData<List<AlbumDetails>> getAllAlbum()
    {
        return getAllAlbum;
    }

   static class InsertAsynTask extends AsyncTask<List<AlbumDetails>,Void,Void>{
        private AlbumDao albumDao;
         InsertAsynTask(AlbumDatabase albumDatabase)
         {
             albumDao=albumDatabase.albumDao();
         }
        @Override
        protected Void doInBackground(List<AlbumDetails>... lists) {
            albumDao.insert(lists[0]);
            return null;
        }
    }
    static class DeleteAsynTask extends AsyncTask<Void,Void,Void>{
        private AlbumDao albumDao;
        DeleteAsynTask(AlbumDatabase albumDatabase)
        {
            albumDao=albumDatabase.albumDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            albumDao.deleteAll();
            return null;
        }
    }

}
