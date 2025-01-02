package com.tool.mediadata.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * desc:
 **
 * user: xujj
 * time: 2023/4/14 17:16
 **/
@Parcelize
data class Album(
    var id: Long,
    var name: String,
    var artistId: Long,
    var artistName: String,
    var musicCount: Int
) : Parcelable {

    companion object {
        fun example(name: String = "xxx", artistName: String = "xxx"): Album {
            return Album(
                -1L,
                name,
                -1L,
                artistName,
                0
            )
        }
    }
}