package com.xu.mediadatatest.entity;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/8/26 14:35
 **/
public class Artist {

    private long id;
    private String name;
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

    public int getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(int musicCount) {
        this.musicCount = musicCount;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", musicCount=" + musicCount +
                '}';
    }
}
