package com.kanika.radioplayerapp.Database;


import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kanika.radioplayerapp.Dao.AlbumDao;
import com.kanika.radioplayerapp.Model.AlbumDetails;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */
@Database(entities = {AlbumDetails.class}, version = 3)
public abstract class AlbumDatabase extends RoomDatabase {
    private static final String DATABASE_NAME="AlbumDatabase";

    public abstract AlbumDao albumDao();

    private static volatile AlbumDatabase INSTANCE;

    public static AlbumDatabase getInstance(Context context){
        if(INSTANCE == null)
        {
            synchronized (AlbumDatabase.class){
                if(INSTANCE == null)
                {
                    INSTANCE= Room.databaseBuilder(context,AlbumDatabase.class,
                            DATABASE_NAME)
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static Callback callback=new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsynTask(INSTANCE);
        }
    };
    static class PopulateAsynTask extends AsyncTask<Void,Void,Void>
    {
        private AlbumDao albumDao;
        PopulateAsynTask(AlbumDatabase albumDatabase)
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
