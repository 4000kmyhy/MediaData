package com.xu.mediadata.entity;

import android.text.TextUtils;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/4/12 11:01
 **/
public class Music {

    private long id;
    private long artistId;
    private long albumId;
    private String title;
    private String artist;
    private String album;
    private String displayName;
    private String data;
    private long duration;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取无后缀文件名
     */
    public String getDisplayNameNoEx() {
        if (!TextUtils.isEmpty(displayName)) {
            int dot = displayName.lastIndexOf('.');
            if (dot > -1) {
                return displayName.substring(0, dot);
            }
        }
        return displayName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", duration=" + duration +
                '}';
    }
}
