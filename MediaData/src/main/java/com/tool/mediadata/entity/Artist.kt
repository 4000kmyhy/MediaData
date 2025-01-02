package com.tool.mediadata.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * desc:
 **
 * user: xujj
 * time: 2023/4/14 17:14
 **/
@Parcelize
data class Artist(
    var id: Long,
    var name: String,
    var musicCount: Int
) : Parcelable {

    companion object {
        fun example(name: String = "xxx"): Artist {
            return Artist(-1L, name, 0)
        }
    }
}