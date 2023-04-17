package com.tool.mediadata.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.tool.mediadata.entity.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * desc:传入music类删除歌曲，通过id删除媒体库，通过data删除文件
 * *
 * user: xujj
 * time: 2021/5/6 17:50
 **/
public class DeleteUtils {

    public final static int DELETE_MUSIC_CODE = 0x02;

    public interface OnDeleteCallback {
        void successOne(long id);

        void success();

        void failed();
    }

    private static OnDeleteCallback onDeleteCallback;

    public static void setOnDeleteCallback(OnDeleteCallback callback) {
        onDeleteCallback = callback;
    }

    public static void deleteMedia(Activity activity, Music music, OnDeleteCallback callback) {
        List<Music> musics = new ArrayList<>();
        musics.add(music);
        deleteMedias(activity, musics, callback);
    }

    public static void deleteMedias(Activity activity, List<Music> musics, OnDeleteCallback callback) {
        setOnDeleteCallback(callback);
        createDeleteRequest(activity, musics);
    }

    private static void createDeleteRequest(Activity activity, List<Music> musics) {
        if (activity == null) return;

        List<Music> failedMusics = getFailedMusics(activity, musics);
        if (!failedMusics.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                OnResultCallback callback = new OnResultCallback() {
                    @Override
                    public void grant() {
                        for (Music music : failedMusics) {
                            if (onDeleteCallback != null) {
                                onDeleteCallback.successOne(music.getId());
                                File file = new File(music.getData());
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                        }
                        if (onDeleteCallback != null) {
                            onDeleteCallback.success();
                            setOnDeleteCallback(null);
                        }
                    }

                    @Override
                    public void refuse() {
                        if (onDeleteCallback != null) {
                            onDeleteCallback.failed();
                            setOnDeleteCallback(null);
                        }
                    }
                };
                setOnResultCallback(callback);
                try {
                    List<Uri> uris = new ArrayList<>();
                    for (Music music : failedMusics) {
                        Uri targetUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, music.getId());
                        uris.add(targetUri);
                    }
                    PendingIntent pendingIntent = MediaStore.createDeleteRequest(activity.getContentResolver(), uris);
                    activity.startIntentSenderForResult(pendingIntent.getIntentSender(), DELETE_MUSIC_CODE, null, 0, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (onDeleteCallback != null) {
                        onDeleteCallback.failed();
                        setOnDeleteCallback(null);
                    }
                }
            } else {
                if (onDeleteCallback != null) {
                    onDeleteCallback.failed();
                    setOnDeleteCallback(null);
                }
            }
        }
    }

    private static List<Music> getFailedMusics(Activity activity, List<Music> musics) {
        List<Music> failedMusics = new ArrayList<>();
        if (activity == null) return failedMusics;
        int deleteCount = 0;
        for (Music music : musics) {
            Uri targetUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, music.getId());
            try {
                int result = activity.getContentResolver().delete(targetUri, null, null);
                if (result >= 1) {
                    if (onDeleteCallback != null) {
                        onDeleteCallback.successOne(music.getId());
                        File file = new File(music.getData());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    deleteCount++;
                    if (deleteCount >= musics.size()) {
                        if (onDeleteCallback != null) {
                            onDeleteCallback.success();
                            setOnDeleteCallback(null);
                        }
                    }
                } else {
                    if (onDeleteCallback != null) {
                        onDeleteCallback.failed();
                        setOnDeleteCallback(null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                failedMusics.add(music);
            }
        }
        return failedMusics;
    }

    private interface OnResultCallback {
        void grant();

        void refuse();
    }

    private static OnResultCallback onResultCallback;

    private static void setOnResultCallback(OnResultCallback callback) {
        onResultCallback = callback;
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (requestCode == DELETE_MUSIC_CODE) {
            if (resultCode == -1) {
                if (onResultCallback != null) {
                    onResultCallback.grant();
                    setOnResultCallback(null);
                }
            } else {
                if (onResultCallback != null) {
                    onResultCallback.refuse();
                    setOnResultCallback(null);
                }
            }
        }
    }
}