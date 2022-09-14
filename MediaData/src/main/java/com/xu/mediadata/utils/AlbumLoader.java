package com.xu.mediadatatest.utils;

import android.content.Context;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.xu.mediadatatest.entity.Album;
import com.xu.mediadatatest.entity.Music;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/8/26 9:49
 **/
public class AlbumLoader {

    /**
     * 通过歌曲列表获取专辑列表
     *
     * @param musics //按专辑排序的歌曲列表
     */
    private static List<Album> getAlbumList(List<Music> musics, String name) {
        List<Album> albumList = new ArrayList<>();
        Map<Long, Album> albumMap = new LinkedHashMap<>();
        try {
            for (Music music : musics) {
                long albumId = music.getAlbumId();
                long artistId = music.getArtistId();
                String albumName = music.getAlbum();
                String artistName = music.getArtist();

                Album album = albumMap.get(albumId);
                if (album == null) {
                    album = new Album();
                    album.setId(albumId);
                    album.setName(albumName);
                    album.setArtistId(artistId);
                    album.setArtistName(artistName);
                    album.setMusicCount(1);
                    albumMap.put(albumId, album);
                } else {
                    album.setMusicCount(album.getMusicCount() + 1);
                    albumMap.put(albumId, album);
                }
            }
            Set<Long> set = albumMap.keySet();
            for (Long albumId : set) {
                Album album = albumMap.get(albumId);
                if (album != null) {
                    if (TextUtils.isEmpty(name)) {
                        albumList.add(album);
                    } else {
                        if (album.getName().toLowerCase().contains(name.toLowerCase())) {
                            albumList.add(album);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albumList;
    }

    public static List<Album> getAlbumList(Context context, String name) {
        List<Music> musics = MusicLoader.getMusicList(context, null, MediaStore.Audio.Media.ALBUM_KEY, null);
        return getAlbumList(musics, name);
    }

    public static List<Album> getAlbumList(Context context) {
        return getAlbumList(context, null);
    }
}
