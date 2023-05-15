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
    var title: String?,
    var artist: String?,
    var album: String?,
    var displayName: String?,
    var data: String,
    var duration: Long
) : Parcelable {

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

    override fun toString(): String {
        return "Music(id=$id, title='$title', artist='$artist', album='$album', data='$data', artistId='$artistId', albumId='$albumId')"
    }
}