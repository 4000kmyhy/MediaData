package com.tool.mediadata.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * desc:
 **
 * user: xujj
 * time: 2023/11/14 15:44
 **/
@Parcelize
data class OnlineGenres(
    var id: Long,
    var name: String,
    var name_cn: String
) : Parcelable