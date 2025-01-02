package com.tool.mediadata.entity

import android.os.Parcelable
import android.text.TextUtils
import androidx.compose.runtime.Immutable
import com.tool.mediadata.MediaConfig
import kotlinx.parcelize.Parcelize
import java.io.File
import java.util.UUID

/**
 * desc:
 **
 * user: xujj
 * time: 2023/4/14 16:49
 **/
@Immutable
@Parcelize
data class Music(
    var id: Long,
    var artistId: Long,
    var albumId: Long,
    @Deprecated("use name") var title: String,
    var artist: String,
    var album: String,
    var displayName: String,
    var data: String,
    var duration: Long,
    var dateAdded: Long,
    var type: Int = TYPE_LOCAL,
    var genres: String = "",
    var genresId: Long = -1L,
    var playUrl: String = "",
    var coverUrl: String = "",
    var downloadUrl: String = "",
    val uuid: String = UUID.randomUUID().toString()
) : Parcelable {

    companion object {
        private const val TYPE_LOCAL = 0
        private const val TYPE_ONLINE = 1

        fun example(title: String = "xxx", artist: String = "xxx") = Music(
            id = -1L,
            artistId = -1L,
            albumId = -1L,
            title = title,
            artist = artist,
            album = "",
            displayName = "",
            data = "",
            duration = 0L,
            dateAdded = 0L,
            type = TYPE_LOCAL
        )

        fun Music.new() = this.copy(uuid = UUID.randomUUID().toString())
    }

    constructor(
        title: String,
        artist: String,
        genres: String,
        genresId: Long,
        playUrl: String,
        coverUrl: String,
        downloadUrl: String,
        duration: Long,
        data: String
    ) : this(
        -1,
        -1,
        -1,
        title,
        artist,
        "",
        "",
        data,
        duration,
        0L,
        TYPE_ONLINE,
        genres,
        genresId,
        playUrl,
        coverUrl,
        downloadUrl
    )

    /**
     * 获取无后缀文件名
     */
    fun getDisplayNameNoEx(): String {
        if (!TextUtils.isEmpty(displayName)) {
            val dot = displayName.lastIndexOf('.')
            if (dot > -1) {
                return displayName.substring(0, dot)
            }
        }
        return displayName
    }

    fun getExtension(): String {
        if (!TextUtils.isEmpty(displayName)) {
            val dot = displayName.lastIndexOf('.')
            if (dot > -1) {
                return displayName.substring(dot)
            }
        }
        return ""
    }

    fun isLocal(): Boolean = type == TYPE_LOCAL
    fun isOnline(): Boolean = type == TYPE_ONLINE

    fun isExists(): Boolean = File(data).exists()

    val name: String
        get() = if (MediaConfig.getInstance().useDisplayNameOrTitle) {
            getDisplayNameNoEx()
        } else {
            title
        }

    fun rename(
        name: String,
        displayName: String,
        data: String
    ) {
        this.title = name
        this.displayName = displayName
        this.data = data
    }

    override fun toString(): String {
        return "Music(" +
                "id=$id, " +
                "title='$title', " +
                "artist='$artist', " +
//                "data='$data', " +
                "uuid='$uuid'" +
                ")"
    }


}