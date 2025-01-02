package com.tool.mediadata

import android.content.Context
import android.text.TextUtils
import com.tool.mediadata.utils.SortOrder

/**
 * desc:
 **
 * user: xujj
 * time: 2024/8/15 18:16
 **/

class MediaConfig {

    var useDisplayNameOrTitle = false

    var sortOrder: String? = SortOrder.DATE_ADDED_DESC
    var artistSortOrder: String? = SortOrder.ArtistSortOrder.ARTIST_A_Z
        private set
    var albumSortOrder: String? = SortOrder.AlbumSortOrder.ALBUM_A_Z
        private set
    var folderSortOrder: String? = SortOrder.FolderSortOrder.FOLDER_A_Z
        private set

    var filterDuration = 0

    fun init(context: Context?) {
        sortOrder = getSP(context, KEY_MUSIC_SORT_ORDER, SortOrder.DATE_ADDED_DESC)
    }

    fun initAll(
        context: Context?,
        useDisplayNameOrTitle: Boolean = false,
        defaultFilterDuration: Int = 0
    ) {
        sortOrder = getSP(context, KEY_MUSIC_SORT_ORDER, SortOrder.DATE_ADDED_DESC)
        artistSortOrder =
            getSP(context, KEY_ARTIST_SORT_ORDER, SortOrder.ArtistSortOrder.ARTIST_A_Z)
        albumSortOrder = getSP(context, KEY_ALBUM_SORT_ORDER, SortOrder.AlbumSortOrder.ALBUM_A_Z)
        folderSortOrder =
            getSP(context, KEY_FOLDER_SORT_ORDER, SortOrder.FolderSortOrder.FOLDER_A_Z)

        this.useDisplayNameOrTitle = useDisplayNameOrTitle

        filterDuration = getSP(context, KEY_FILTER_DURATION, defaultFilterDuration)
    }

    fun setSortOrder(context: Context?, sortOrder: String) {
        if (!TextUtils.equals(this.sortOrder, sortOrder)) {
            this.sortOrder = sortOrder
            setSP(context, KEY_MUSIC_SORT_ORDER, sortOrder)
        }
    }

    fun setArtistSortOrder(context: Context?, sortOrder: String) {
        if (artistSortOrder != sortOrder) {
            artistSortOrder = sortOrder
            setSP(context, KEY_ARTIST_SORT_ORDER, sortOrder)
        }
    }

    fun setAlbumSortOrder(context: Context?, sortOrder: String) {
        if (albumSortOrder != sortOrder) {
            albumSortOrder = sortOrder
            setSP(context, KEY_ALBUM_SORT_ORDER, sortOrder)
        }
    }

    fun setFolderSortOrder(context: Context?, sortOrder: String) {
        if (folderSortOrder != sortOrder) {
            folderSortOrder = sortOrder
            setSP(context, KEY_FOLDER_SORT_ORDER, sortOrder)
        }
    }

    fun setFilterDuration(context: Context?, duration: Int) {
        if (filterDuration != duration) {
            filterDuration = duration
            setSP(context, KEY_FILTER_DURATION, duration)
        }
    }

    companion object {
        private var mInstance: MediaConfig? = null

        fun getInstance(): MediaConfig {
            if (mInstance == null) {
                synchronized(MediaConfig::class.java) {
                    mInstance = MediaConfig()
                }
            }
            return mInstance!!
        }

        private const val KEY_MUSIC_SORT_ORDER = "music_sort_order"
        private const val KEY_ARTIST_SORT_ORDER = "artist_sort_order"
        private const val KEY_ALBUM_SORT_ORDER = "album_sort_order"
        private const val KEY_FOLDER_SORT_ORDER = "folder_sort_order"

        private const val KEY_FILTER_DURATION = "filter_duration"

        private const val SHARE_DEF_NAME = "MediaConfig"

        private fun getSP(context: Context?, key: String, defValue: String?): String? {
            if (context == null) return defValue
            val sp = context.getSharedPreferences(SHARE_DEF_NAME, Context.MODE_PRIVATE)
            return sp.getString(key, defValue)
        }

        private fun setSP(context: Context?, key: String, value: String) {
            if (context == null) return
            val sp = context.getSharedPreferences(SHARE_DEF_NAME, Context.MODE_PRIVATE)
            sp.edit().putString(key, value).apply()
        }

        private fun getSP(context: Context?, key: String, defValue: Int): Int {
            if (context == null) return defValue
            val sp = context.getSharedPreferences(SHARE_DEF_NAME, Context.MODE_PRIVATE)
            return sp.getInt(key, defValue)
        }

        private fun setSP(context: Context?, key: String, value: Int) {
            if (context == null) return
            val sp = context.getSharedPreferences(SHARE_DEF_NAME, Context.MODE_PRIVATE)
            sp.edit().putInt(key, value).apply()
        }
    }
}
