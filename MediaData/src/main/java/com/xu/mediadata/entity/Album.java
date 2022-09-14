package com.xu.mediadata.entity;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/8/26 12:00
 **/
public class Album {

    private long id;
    private String name;
    private long artistId;
    private String artistName;
    private int musicCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(int musicCount) {
        this.musicCount = musicCount;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                ", musicCount=" + musicCount +
                '}';
    }
}
