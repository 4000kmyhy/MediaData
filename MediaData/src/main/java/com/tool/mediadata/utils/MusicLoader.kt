package com.tool.mediadata.utils

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.text.TextUtils
import com.tool.mediadata.MediaConfig
import com.tool.mediadata.database.PlaylistOpenHelper
import com.tool.mediadata.entity.Music
import java.io.File

/**
 * desc:
 **
 * user: xujj
 * time: 2023/5/5 9:08
 **/
object MusicLoader {

    private val mediaColumns = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DURATION,
    )

    @JvmStatic
    fun getMusicList(
        context: Context?,
        selection: String?,
        sortOrder: String?,
        dir: String?
    ): MutableList<Music> {
        val musicList = ArrayList<Music>()
        if (context == null) return musicList
        val sb = StringBuilder()
        sb.append(MediaStore.Audio.Media.DURATION + " > 0")
        sb.append(" AND " + MediaStore.Audio.Media.ARTIST_ID + " > 0")
        sb.append(" AND " + MediaStore.Audio.Media.ALBUM_ID + " > 0")
        sb.append(" AND " + MediaStore.Audio.Media.TITLE + " != ''")
        sb.append(" AND " + MediaStore.Audio.Media.ARTIST + " != 'null'")
        sb.append(" AND " + MediaStore.Audio.Media.ALBUM + " != 'null'")
        if (!TextUtils.isEmpty(selection)) {
            sb.append(" AND $selection")
        }
        if (!TextUtils.isEmpty(dir)) {
            sb.append(" AND " + MediaStore.Audio.Media.DATA + " LIKE '" + dir + "%'")
        }
        var mSortOrder = sortOrder
        if (TextUtils.isEmpty(mSortOrder)) {
            mSortOrder = SortOrder.TITLE_A_Z
        }

        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                sb.toString(), null, mSortOrder
            )
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val artistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))
                    val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)) ?: ""
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) ?: ""
                    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)) ?: ""
                    val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

                    //文件不存在
                    if (!File(data).exists()) {
                        continue
                    }
                    if (!TextUtils.isEmpty(dir)) {
                        //有文件夹路径时过滤文件夹
                        if (!TextUtils.equals(data, "$dir/$displayName")) {
                            continue
                        }
                    }

                    val music = Music(
                        id,
                        artistId,
                        albumId,
                        title,
                        artist,
                        album,
                        displayName,
                        data,
                        duration
                    )
                    musicList.add(music)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return musicList
    }

    @JvmStatic
    fun getAllMusicList(context: Context?): MutableList<Music> {
        return getMusicList(context, null, MediaConfig.getInstance().sortOrder, null)
    }

    @JvmStatic
    fun getMusicListByName(context: Context?, name: String?): MutableList<Music> {
        var selection: String? = null
        if (!name.isNullOrEmpty()) {
            selection = MediaStore.Audio.Media.TITLE + " LIKE '%" + name.lowercase() + "%'"
        }
        return getMusicList(context, selection, MediaConfig.getInstance().sortOrder, null)
    }

    /**
     * 通过歌名、歌手名搜索音乐
     */
    @JvmStatic
    fun getMusicListByTitleOrArtist(context: Context?, name: String?): MutableList<Music> {
        var selection: String? = null
        if (!name.isNullOrEmpty()) {
            selection = MediaStore.Audio.Media.TITLE + " LIKE '%" + name.lowercase() + "%'" + " OR " +
                    MediaStore.Audio.Media.ARTIST + " LIKE '%" + name.lowercase() + "%'"
        }
        return getMusicList(context, selection, MediaConfig.getInstance().sortOrder, null)
    }

    @JvmStatic
    fun getMusicListByArtistId(context: Context?, artistId: Long): MutableList<Music> {
        val selection = MediaStore.Audio.Media.ARTIST_ID + " = " + artistId
        return getMusicList(context, selection, MediaConfig.getInstance().sortOrder, null)
    }

    @JvmStatic
    fun getMusicListByAlbumId(context: Context?, albumId: Long): MutableList<Music> {
        val selection = MediaStore.Audio.Media.ALBUM_ID + " = " + albumId
        return getMusicList(context, selection, MediaConfig.getInstance().sortOrder, null)
    }

    @JvmStatic
    fun getMusicListByPath(context: Context?, path: String?): MutableList<Music> {
        return getMusicList(context, null, MediaConfig.getInstance().sortOrder, path)
    }

    @JvmStatic
    fun getMusicListByPath(
        context: Context?,
        path: String?,
        sortOrder: String?
    ): MutableList<Music> {
        return getMusicList(context, null, sortOrder, path)
    }

    @JvmStatic
    fun isExit(context: Context?, path: String): Boolean {
        if (context == null || TextUtils.isEmpty(path)) return false
        val selection = MediaStore.Audio.Media.DATA + " LIKE '" + path + "'"
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                selection, null, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return false
    }

    /**
     * 根据id列表获取歌曲
     */
    @JvmStatic
    fun getMusicListByIds(context: Context?, ids: List<Long?>?): MutableList<Music> {
        if (ids == null) return ArrayList()
        val sb = StringBuilder()
        sb.append("_id IN (")
        for (i in ids.indices) {
            sb.append(ids[i])
            if (i < ids.size - 1) {
                sb.append(",")
            }
        }
        sb.append(")")
        return getMusicList(context, sb.toString(), MediaConfig.getInstance().sortOrder, null)
    }

    /**
     * 根据id列表获取歌曲
     */
    @JvmStatic
    fun getMusicListNotByIds(context: Context?, ids: List<Long?>?): MutableList<Music> {
        if (ids == null) return ArrayList()
        val sb = StringBuilder()
        sb.append("_id NOT IN (")
        for (i in ids.indices) {
            sb.append(ids[i])
            if (i < ids.size - 1) {
                sb.append(",")
            }
        }
        sb.append(")")
        return getMusicList(context, sb.toString(), MediaConfig.getInstance().sortOrder, null)
    }

    /**
     * 根据id列表获取歌曲，并按id列表排序（可重复）
     */
    @JvmStatic
    fun getMusicListOrderByIds(context: Context?, ids: List<Long?>?): MutableList<Music> {
        if (ids == null) return ArrayList()
        val newList = ArrayList<Music>()
        val musicList = getMusicListByIds(context, ids)
        val map: Map<Long, Music> = musicList.associateBy { it.id }
        for (id in ids) {
//            musicList.find { it.id == id }?.let {
//                newList.add(it)
//            }
            map.get(id)?.let { newList.add(it) }
        }
        return newList
    }

    @JvmStatic
    fun getMusicIdsList(musicList: List<Music?>?): MutableList<Long>? {
        if (musicList == null) return null
        val ids: MutableList<Long> = ArrayList()
        for (music in musicList) {
            if (music != null) {
                ids.add(music.id)
            } else {
                ids.add(-1L)
            }
        }
        return ids
    }

    @JvmStatic
    fun getMusicIdsArray(musicList: List<Music?>?): LongArray? {
        if (musicList == null) return null
        val ids = LongArray(musicList.size)
        for (i in musicList.indices) {
            if (musicList[i] != null) {
                ids[i] = musicList[i]!!.id
            } else {
                ids[i] = -1
            }
        }
        return ids
    }

    fun getMusicByPath(context: Context?, path: String): Music? {
        val selection = MediaStore.Audio.Media.DATA + " = '" + path + "'"
        val musicList = getMusicList(context, selection, null, null)
        return if (musicList.isEmpty()) {
            null
        } else {
            musicList.get(0)
        }
    }

    fun getMusicById(context: Context?, id: Long): Music? {
        val selection = MediaStore.Audio.Media._ID + " = " + id
        val musicList = getMusicList(context, selection, null, null)
        return if (musicList.isEmpty()) {
            null
        } else {
            musicList.get(0)
        }
    }

    fun getArtistMusicList(musicList: MutableList<Music>?, artistId: Long): MutableList<Music>? {
        return musicList?.filter {
            artistId == it.artistId
        }?.toMutableList()
    }

    fun getAlbumMusicList(musicList: MutableList<Music>?, albumId: Long): MutableList<Music>? {
        return musicList?.filter {
            albumId == it.albumId
        }?.toMutableList()
    }

    fun getFolderMusicList(musicList: MutableList<Music>?, dir: String): MutableList<Music>? {
        return musicList?.filter {
            TextUtils.equals(it.data, "$dir/${it.displayName}")
        }?.toMutableList()
    }

    fun getPlaylistMusicList(context: Context, musicList: MutableList<Music>?, playlistId: Long): MutableList<Music>? {
        if (musicList != null) {
            val musicIds = PlaylistOpenHelper(context).queryMusicList(playlistId)
            val newList = ArrayList<Music>()
            for (music in musicList) {
                if (musicIds.contains(music.id)) {
                    newList.add(music)
                }
            }
            return newList
        }
        return null
    }

    fun getNotPlaylistMusicList(context: Context, musicList: MutableList<Music>?, playlistId: Long): MutableList<Music>? {
        if (musicList != null) {
            val musicIds = PlaylistOpenHelper(context).queryMusicList(playlistId)
            val newList = ArrayList<Music>()
            newList.addAll(musicList)
            for (music in musicList) {
                if (musicIds.contains(music.id)) {
                    newList.remove(music)
                }
            }
            return newList
        }
        return null
    }
}