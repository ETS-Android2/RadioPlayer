package com.kanika.radioplayerapp.Model;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kanika Tandel on 07/03/2022.
 */
@Entity(tableName = "AlbumDetails", indices = @Index(value = {"albumId"},unique = true))
public class AlbumDetails {
    @PrimaryKey(autoGenerate = true)
    private int albumId;
    @SerializedName("sid")
    @Expose
    private String sid;
    @SerializedName("album")
    @Expose
    @ColumnInfo(name = "album")
    private String album;
    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    private String name;
    @SerializedName("artist")
    @Expose
    @ColumnInfo(name = "artist")
    private String artist;
    @SerializedName("image_url")
    @Expose
    @ColumnInfo(name = "imageUrl")
    private String imageUrl;
    @SerializedName("link_url")
    @Expose
    @ColumnInfo(name = "linkUrl")
    private String linkUrl;
    @SerializedName("preview_url")
    @Expose
    @ColumnInfo(name = "previewUrl")
    private String previewUrl;

    @SerializedName("played_at")
    @Expose
    @ColumnInfo(name = "playedAt")
    private String playedAt;

//    public AlbumDetails(int sid, String album, String name, String artist,String imageUrl,
//                        String linkUrl,String previewUrl,String playedAt) {
//        this.sid = sid;
//        this.album = album;
//        this.name = name;
//        this.artist = artist;
//        this.imageUrl=imageUrl;
//        this.linkUrl=linkUrl;
//        this.previewUrl=previewUrl;
//        this.playedAt=playedAt;
//
//
//    }


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(String playedAt) {
        this.playedAt = playedAt;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }
//    @Override
//    public String toString() {
//        return "AlbumDetails{" +
//                "sid=" + sid +
//                ", album=" + album +
//                ", name='" + name + '\'' +
//                ", artist='" + artist + '\'' +
//                ", imageUrl=" + imageUrl +
//                ", linkUrl=" + linkUrl +
//                ", previewUrl=" + previewUrl +
//                ", playedAt=" + playedAt +
//                '}';
//    }

}