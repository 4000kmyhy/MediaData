package com.xxx.mediadata;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.xxx.mediadata.utils.SortOrder;

/**
 * desc:
 * *
 * user: xujj
 * time: 2022/8/26 12:01
 **/
public class MediaConfig {

    private static MediaConfig mInstance;

    public static MediaConfig getInstance() {
        if (mInstance == null) {
            synchronized (MediaConfig.class) {
                if (mInstance == null) {
                    mInstance = new MediaConfig();
                }
            }
        }
        return mInstance;
    }

    public String sortOrder;

    public void init(Context context) {
        sortOrder = (String) get(context, "music_sort_order", SortOrder.TITLE_A_Z);
    }

    public void setSortOrder(Context context, String sortOrder) {
        if (!TextUtils.equals(this.sortOrder, sortOrder)) {
            this.sortOrder = sortOrder;
            set(context, "music_sort_order", sortOrder);
        }
    }

    private static final String SHARE_DEF_NAME = "MediaConfig";

    private static Object get(Context context, String key, Object defValue) {
        if (context == null) return defValue;
        SharedPreferences sp = context.getSharedPreferences(SHARE_DEF_NAME, Context.MODE_PRIVATE);

        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        }
        return defValue;
    }

    private static void set(Context context, String key, Object value) {
        if (context == null) return;
        SharedPreferences sp = context.getSharedPreferences(SHARE_DEF_NAME, Context.MODE_PRIVATE);

        if (value instanceof String) {
            sp.edit().putString(key, (String) value).apply();
        } else if (value instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Integer) {
            sp.edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof Long) {
            sp.edit().putLong(key, (Long) value).apply();
        } else if (value instanceof Float) {
            sp.edit().putFloat(key, (Float) value).apply();
        }
    }
}
