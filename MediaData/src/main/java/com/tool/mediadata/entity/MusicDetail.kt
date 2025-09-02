package com.tool.mediadata.entity

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

/**
 * desc:
 **
 * user: xujj
 * time: 2025/6/5 9:47
 **/
@Immutable
@Parcelize
data class MusicDetail(
    val id: Long,
    val name: String,
    val artist: String,
    val album: String
) : Parcelable {

}
