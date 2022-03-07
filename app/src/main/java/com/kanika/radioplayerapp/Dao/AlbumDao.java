package com.kanika.radioplayerapp.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.kanika.radioplayerapp.Model.AlbumDetails;

import java.util.List;
/**
 * Created by Kanika Tandel on 07/03/2022.
 */
@Dao
public interface AlbumDao {
    /**
     * Insert all songs.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AlbumDetails> albumList);

    /**
     * Select top ten recently played  songs from the AlbumDetails table.
     *
     * @return top ten recently played  songsList .
     */
    @Query("SELECT * FROM AlbumDetails ORDER BY albumId DESC LIMIT 10")
    LiveData<List<AlbumDetails>> getAllAlbumlist();

    /**
     * Delete all songs.
     */
    @Query("DELETE FROM AlbumDetails")
    void deleteAll();
}


