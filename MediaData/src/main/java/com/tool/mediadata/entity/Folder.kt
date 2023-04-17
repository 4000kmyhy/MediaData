package com.tool.mediadata.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * desc:
 **
 * user: xujj
 * time: 2023/4/14 17:08
 **/
@Parcelize
data class Folder(
    var name: String,
    var path: String,
    var musicCount: Int
) : Parcelable