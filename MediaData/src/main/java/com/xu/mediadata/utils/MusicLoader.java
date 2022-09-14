package com.xu.mediadatatest.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.xu.mediadatatest.MediaConfig;
import com.xu.mediadatatest.entity.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/4/12 10:51
 **/
public class MusicLoader {

    private static final String[] mediaColumns = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
    };

    public static List<Music> getMusicList(Context context, String selection, String sortOrder, String dir) {
        List<Music> musicList = new ArrayList<>();
        if (context == null) return musicList;

        StringBuilder sb = new StringBuilder();
        sb.append(MediaStore.Audio.Media.DURATION + " > 0");
        if (!TextUtils.isEmpty(selection)) {
            sb.append(" AND " + selection);
        }
        if (!TextUtils.isEmpty(dir)) {
            sb.append(" AND " + MediaStore.Audio.Media.DATA + " LIKE '" + dir + "%'");
        }
        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = SortOrder.TITLE_A_Z;
        }

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                    sb.toString(), null, sortOrder);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    long artistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
                    long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                    if (!TextUtils.isEmpty(dir)) {
                        //有文件夹路径时过滤文件夹
                        if (!TextUtils.equals(data, dir + "/" + displayName)) {
                            continue;
                        }
                    }

                    Music music = new Music();
                    music.setId(id);
                    music.setArtistId(artistId);
                    music.setAlbumId(albumId);
                    music.setTitle(title);
                    music.setArtist(artist);
                    music.setAlbum(album);
                    music.setData(data);
                    music.setDisplayName(displayName);
                    music.setDuration(duration);

                    musicList.add(music);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return musicList;
    }

    public static List<Music> getAllMusicList(Context context) {
        return getMusicList(context, null, MediaConfig.getInstance().sortOrder, null);
    }

    public static List<Music> getMusicListByName(Context context, String name) {
        String selection = null;
        if (!TextUtils.isEmpty(name)) {
            selection = MediaStore.Audio.Media.TITLE + " LIKE '%" + name.toLowerCase() + "%'";
        }
        return getMusicList(context, selection, MediaConfig.getInstance().sortOrder, null);
    }

    public static List<Music> getMusicListByArtistId(Context context, long artistId) {
        String selection = MediaStore.Audio.Media.ARTIST_ID + " = " + artistId;
        return getMusicList(context, selection, MediaConfig.getInstance().sortOrder, null);
    }

    public static List<Music> getMusicListByAlbumId(Context context, long albumId) {
        String selection = MediaStore.Audio.Media.ALBUM_ID + " = " + albumId;
        return getMusicList(context, selection, MediaConfig.getInstance().sortOrder, null);
    }

    public static List<Music> getMusicListByPath(Context context, String path) {
        return getMusicList(context, null, MediaConfig.getInstance().sortOrder, path);
    }

    public static List<Music> getMusicListByPath(Context context, String path, String sortOrder) {
        return getMusicList(context, null, sortOrder, path);
    }

    public static boolean isExit(Context context, String path) {
        if (context == null || TextUtils.isEmpty(path)) return false;

        String selection = MediaStore.Audio.Media.DATA + " LIKE '" + path + "'";

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                    selection, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }
}
