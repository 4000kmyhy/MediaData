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
    var name: String?,
    var artistId: Long,
    var artistName: String?,
    var musicCount: Int
) : Parcelable