package com.tool.mediadata.entity

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.parcelize.Parcelize

/**
 * desc:
 **
 * user: xujj
 * time: 2023/4/14 16:49
 **/
@Parcelize
data class Music(
    var id: Long,
    var artistId: Long,
    var albumId: Long,
    var title: String,
    var artist: String,
    var album: String,
    var displayName: String?,
    var data: String,
    var duration: Long,
    var type: Int = TYPE_LOCAL
) : Parcelable {

    companion object {
        private const val TYPE_LOCAL = 0
        private const val TYPE_ONLINE = 1
    }

    var genres: String = ""
    var genresId = -1L
    var playUrl: String = ""
    var coverUrl: String = ""
    var downloadUrl: String = ""
    var isLock = false

    constructor(
        title: String,
        artist: String,
        genres: String,
        genresId: Long,
        playUrl: String,
        coverUrl: String,
        downloadUrl: String,
        duration: Long,
        isLock: Boolean
    ) : this(
        -1,
        -1,
        -1,
        title,
        artist,
        "",
        null,
        "",
        duration,
        TYPE_ONLINE
    ) {
        this.genres = genres
        this.genresId = genresId
        this.playUrl = playUrl
        this.coverUrl = coverUrl
        this.downloadUrl = downloadUrl
        this.isLock = isLock
    }

    /**
     * 获取无后缀文件名
     */
    fun getDisplayNameNoEx(): String? {
        if (!TextUtils.isEmpty(displayName)) {
            val dot = displayName!!.lastIndexOf('.')
            if (dot > -1) {
                return displayName!!.substring(0, dot)
            }
        }
        return displayName
    }

    fun isLocal(): Boolean = type == TYPE_LOCAL
    fun isOnline(): Boolean = type == TYPE_ONLINE

//    override fun toString(): String {
//        return "Music(id=$id, title='$title', artist='$artist', album='$album', data='$data', artistId='$artistId', albumId='$albumId')"
//    }
}