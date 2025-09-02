package com.tool.mediadata.utils

import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.tool.mediadata.database.MusicDetailOpenHelper
import java.io.File

/**
 * desc:
 **
 * user: xujj
 * time: 2025/6/5 14:25
 **/
object ModifyUtils {

    const val MODIFY_INFO_CODE: Int = 0x01

    interface OnModifyCallback {
        fun success()

        fun failed()

        fun request()
    }

    /**
     * Android10手机只能在系统指定目录下操作，否则重命名会生成新的id
     */
    fun renameFile(
        activity: Activity?,
        id: Long,
        oldPath: String,
        newPath: String,
        newName: String,
        displayName: String,
        callback: OnModifyCallback?
    ) {
        if (activity == null) return

        try {
            renameFileInternal(activity, id, oldPath, newPath, newName, displayName, callback)
        } catch (e: Exception) {
            e.printStackTrace()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                onResultCallback = object : OnResultCallback {
                    override fun grant() {
                        try {
                            renameFileInternal(
                                activity,
                                id,
                                oldPath,
                                newPath,
                                newName,
                                displayName,
                                callback
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            callback?.failed()
                        }
                    }

                    override fun refuse() {
                        callback?.failed()
                    }
                }
                try {
                    val uris: MutableList<Uri> = ArrayList()
                    val targetUri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    uris.add(targetUri)
                    val pendingIntent =
                        MediaStore.createWriteRequest(activity.contentResolver, uris)
                    activity.startIntentSenderForResult(
                        pendingIntent.intentSender, MODIFY_INFO_CODE,
                        null, 0, 0, 0
                    )
                    callback?.request()
                } catch (e1: Exception) {
                    e1.printStackTrace()
                    callback?.failed()
                }
            } else {
                callback?.failed()
            }
        }
    }

    private fun renameFileInternal(
        activity: Activity,
        id: Long,
        oldPath: String,
        newPath: String,
        newName: String,
        displayName: String,
        callback: OnModifyCallback?
    ) {
        val targetUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        val values = ContentValues()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put("is_pending", 1)
            activity.contentResolver.update(targetUri, values, null, null)
            values.clear()
            values.put("is_pending", 0)
        } else {
            values.put(MediaStore.Audio.Media.DATA, newPath)
        }
        values.put(MediaStore.Audio.Media.TITLE, newName)
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, displayName)
        val result = activity.contentResolver.update(targetUri, values, null, null)
        if (result >= 1) {
            val from = File(oldPath)
            val to = File(newPath)
            //原文件存在并且新文件不存在时重命名文件，Android10以上修改媒体库会自动重命名文件了
            if (from.exists() && !to.exists()) {
                if (from.renameTo(to)) {
                    callback?.success()
                } else {
                    callback?.failed()
                }
            } else {
                callback?.success()
            }
        } else {
            callback?.failed()
        }
    }

    fun modifyInfo(
        activity: Activity?,
        id: Long,
        newTitle: String,
        newArtist: String,
        newAlbum: String,
        callback: OnModifyCallback?
    ) {
        if (activity == null) return
        try {
            modifyInfoInternal(
                activity,
                id,
                newTitle,
                newArtist,
                newAlbum,
                callback
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                onResultCallback = object : OnResultCallback {
                    override fun grant() {
                        try {
                            modifyInfoInternal(
                                activity,
                                id,
                                newTitle,
                                newArtist,
                                newAlbum,
                                callback
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            callback?.failed()
                        }
                    }

                    override fun refuse() {
                        callback?.failed()
                    }
                }
                try {
                    val uris: MutableList<Uri> = ArrayList()
                    val targetUri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    uris.add(targetUri)
                    val pendingIntent =
                        MediaStore.createWriteRequest(activity.contentResolver, uris)
                    activity.startIntentSenderForResult(
                        pendingIntent.intentSender, MODIFY_INFO_CODE,
                        null, 0, 0, 0
                    )
                    callback?.request()
                } catch (e1: Exception) {
                    e1.printStackTrace()
                    callback?.failed()
                }
            } else {
                callback?.failed()
            }
        }
    }

    private fun modifyInfoInternal(
        activity: Activity,
        id: Long,
        newTitle: String,
        newArtist: String,
        newAlbum: String,
        callback: OnModifyCallback?
    ) {
        val targetUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        val values = ContentValues()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Audio.Media.IS_PENDING, 1)
            activity.contentResolver.update(targetUri, values, null, null)
            values.clear()
            values.put(MediaStore.Audio.Media.IS_PENDING, 0)
        }
        values.put(MediaStore.Audio.Media.TITLE, newTitle)
        values.put(MediaStore.Audio.Media.ARTIST, newArtist)
        values.put(MediaStore.Audio.Media.ALBUM, newAlbum)
        val result = activity.contentResolver.update(targetUri, values, null, null)
        if (result >= 1) {
            checkInfo(activity, id, newTitle, newArtist, newAlbum)
            callback?.success()
        } else {
            callback?.failed()
        }
    }

    private fun checkInfo(
        context: Context,
        id: Long,
        title: String,
        artist: String,
        album: String
    ) {
        val music = MusicLoader.getMusicById(context, id)
        if (music != null) {
            if (music.name != title || music.artist != artist || music.album != album) {
                MusicDetailOpenHelper(context).rename(id, title, artist, album)
            }
        }
    }

    interface OnResultCallback {
        fun grant()

        fun refuse()
    }

    private var onResultCallback: OnResultCallback? = null

    fun onActivityResult(context: Context?, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MODIFY_INFO_CODE) {
            if (resultCode == -1) {
                onResultCallback?.grant()
                onResultCallback = null
            } else {
                onResultCallback?.refuse()
                onResultCallback = null
            }
        }
    }
}