package com.xu.mediadatatest.entity;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/8/26 14:53
 **/
public class Folder {

    private String name;
    private String path;
    private int musicCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(int musicCount) {
        this.musicCount = musicCount;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", musicCount=" + musicCount +
                '}';
    }
}
