package com.xu.mediadatatest.utils;

import android.content.Context;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.xu.mediadatatest.entity.Artist;
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
 * time: 2022/8/26 14:45
 **/
public class ArtistLoader {

    /**
     * 通过歌曲列表获取歌手列表
     *
     * @param musics //按歌手排序的歌曲列表
     */
    private static List<Artist> getArtistList(List<Music> musics, String name) {
        List<Artist> artistList = new ArrayList<>();
        Map<Long, Artist> artistMap = new LinkedHashMap<>();
        try {
            for (Music music : musics) {
                long artistId = music.getArtistId();
                String artistName = music.getArtist();

                Artist artist = artistMap.get(artistId);
                if (artist == null) {
                    artist = new Artist();
                    artist.setId(artistId);
                    artist.setName(artistName);
                    artist.setMusicCount(1);
                    artistMap.put(artistId, artist);
                } else {
                    artist.setMusicCount(artist.getMusicCount() + 1);
                    artistMap.put(artistId, artist);
                }
            }
            Set<Long> set = artistMap.keySet();
            for (Long artistId : set) {
                Artist artist = artistMap.get(artistId);
                if (artist != null) {
                    if (TextUtils.isEmpty(name)) {
                        artistList.add(artist);
                    } else {
                        if (artist.getName().toLowerCase().contains(name.toLowerCase())) {
                            artistList.add(artist);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artistList;
    }

    public static List<Artist> getArtistList(Context context, String name) {
        List<Music> musics = MusicLoader.getMusicList(context, null, MediaStore.Audio.Media.ARTIST_KEY, null);
        return getArtistList(musics, name);
    }

    public static List<Artist> getArtistList(Context context) {
        return getArtistList(context, null);
    }
}
