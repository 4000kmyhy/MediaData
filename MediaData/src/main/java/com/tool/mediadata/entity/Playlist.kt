package com.tool.mediadata.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * desc:
 **
 * user: xujj
 * time: 2023/4/28 9:32
 **/
@Parcelize
data class Playlist(
    var id: Long,
    var name: String,
    var musicCount: Int
) : Parcelable {
    override fun toString(): String {
        return "Playlist(id=$id, name='$name', musicCount=$musicCount)"
    }
}