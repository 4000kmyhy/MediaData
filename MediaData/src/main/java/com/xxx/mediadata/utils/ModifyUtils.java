package com.xxx.mediadata.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/4/13 10:14
 **/
public class ModifyUtils {

    public final static int MODIFY_INFO_CODE = 0x01;

    public interface OnModifyCallback {
        void success();

        void failed();
    }

    private static OnModifyCallback onModifyCallback;

    private static void setOnModifyCallback(OnModifyCallback callback) {
        onModifyCallback = callback;
    }

    public static void modifyInfo(Activity activity, long id, String oldPath, String newPath, String displayName, OnModifyCallback callback) {
        if (activity == null) return;
        setOnModifyCallback(callback);

        try {
            modifyInfoInternal(activity, id, oldPath, newPath, displayName);
        } catch (Exception e) {
            e.printStackTrace();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                OnResultCallback onResultCallback = new OnResultCallback() {
                    @Override
                    public void grant() {
                        try {
                            modifyInfoInternal(activity, id, oldPath, newPath, displayName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (onModifyCallback != null) {
                                onModifyCallback.failed();
                                setOnModifyCallback(null);
                            }
                        }
                    }

                    @Override
                    public void refuse() {
                        if (onModifyCallback != null) {
                            onModifyCallback.failed();
                            setOnModifyCallback(null);
                        }
                    }
                };
                setOnResultCallback(onResultCallback);
                try {
                    List<Uri> uris = new ArrayList<>();
                    Uri targetUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                    uris.add(targetUri);
                    PendingIntent pendingIntent = MediaStore.createWriteRequest(activity.getContentResolver(), uris);
                    activity.startIntentSenderForResult(pendingIntent.getIntentSender(), MODIFY_INFO_CODE,
                            null, 0, 0, 0);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    if (onModifyCallback != null) {
                        onModifyCallback.failed();
                        setOnModifyCallback(null);
                    }
                }
            } else {
                if (onModifyCallback != null) {
                    onModifyCallback.failed();
                    setOnModifyCallback(null);
                }
            }
        }
    }

    private static void modifyInfoInternal(Activity activity, long id, String oldPath, String newPath, String displayName) {
        Uri targetUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        ContentValues values = new ContentValues();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put("is_pending", 1);
            activity.getContentResolver().update(targetUri, values, null, null);
            values.clear();
            values.put("is_pending", 0);
        } else {
            values.put(MediaStore.Audio.Media.DATA, newPath);
        }
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, displayName);
        int result = activity.getContentResolver().update(targetUri, values, null, null);
        if (result >= 1) {
            File from = new File(oldPath);
            File to = new File(newPath);
            //原文件存在并且新文件不存在时重命名文件，Android10以上修改媒体库会自动重命名文件了
            if (from.exists() && !to.exists()) {
                if (from.renameTo(to)) {
                    if (onModifyCallback != null) {
                        onModifyCallback.success();
                        setOnModifyCallback(null);
                    }
                } else {
                    if (onModifyCallback != null) {
                        onModifyCallback.failed();
                        setOnModifyCallback(null);
                    }
                }
            } else {
                if (onModifyCallback != null) {
                    onModifyCallback.success();
                    onModifyCallback = null;
                }
            }
        } else {
            if (onModifyCallback != null) {
                onModifyCallback.failed();
                setOnModifyCallback(null);
            }
        }
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
        if (requestCode == MODIFY_INFO_CODE) {
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
