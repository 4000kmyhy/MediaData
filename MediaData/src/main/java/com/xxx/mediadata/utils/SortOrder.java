package com.xxx.mediadata.utils;

import android.provider.MediaStore;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/8/26 17:18
 **/
public class SortOrder {

    public final static String DATE_ADDED_DESC = MediaStore.Audio.Media.DATE_ADDED + " desc";
    public final static String TITLE_A_Z = MediaStore.Audio.Media.TITLE;
    public final static String TITLE_Z_A = MediaStore.Audio.Media.TITLE + " desc";
    public final static String DURATION = MediaStore.Audio.Media.DURATION;
    public final static String DURATION_DESC = MediaStore.Audio.Media.DURATION + " desc";
}
