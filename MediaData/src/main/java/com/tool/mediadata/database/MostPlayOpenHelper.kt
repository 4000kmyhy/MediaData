package com.tool.mediadata.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

/**
 * desc:
 **
 * user: xujj
 * time: 2023/11/15 17:39
 **/
class MostPlayOpenHelper(context: Context?, dbName: String = "mostPlay.db") :
    SQLiteOpenHelper(context, dbName, null, VERSION_CODE) {

    companion object {

        private const val VERSION_CODE = 1

        const val TABLE_MUSIC = "music"
        const val MUSIC_ID = "id"
        const val MUSIC_PATH = "path"
        const val ARTIST_ID = "artist_id"
        const val ALBUM_ID = "album_id"
        const val PLAY_TIMES = "play_times"

        private const val LIMIT = "100"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table IF NOT EXISTS $TABLE_MUSIC (" +
                "$MUSIC_ID integer not null PRIMARY KEY," +
                "$MUSIC_PATH varchar(100) not null," +
                "$ARTIST_ID integer not null," +
                "$ALBUM_ID integer not null," +
                "$PLAY_TIMES integer not null" +
                ")"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private var db: SQLiteDatabase? = null

    fun insert(musicId: Long, path: String, artistId: Long, albumId: Long) {
        try {
            db = writableDatabase

            var playTimes = -1
            val sql1 = "select $PLAY_TIMES from $TABLE_MUSIC where $MUSIC_ID = $musicId"
            db?.rawQuery(sql1, null)?.let {
                if (it.moveToFirst()) {
                    playTimes = it.getInt(it.getColumnIndexOrThrow(PLAY_TIMES))
                }
                it.close()
            }

            if (playTimes < 0) {//不存在
                val sql = "insert into $TABLE_MUSIC " +
                        "values($musicId,'$path',$artistId,$albumId,1)"
                db?.execSQL(sql)
            } else {
                val sql = "update $TABLE_MUSIC " +
                        "set $PLAY_TIMES = ${playTimes + 1} " +
                        "where $MUSIC_ID = $musicId"
                db?.execSQL(sql)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db?.close()
        }
    }

    fun queryMusicIds(): List<Long> {
        val musicIds = ArrayList<Long>()
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val sql = "select $MUSIC_ID,$MUSIC_PATH" +
                    " from $TABLE_MUSIC" +
                    " order by $PLAY_TIMES desc" +
                    " limit $LIMIT"
            cursor = db!!.rawQuery(sql, null)
            if (cursor != null) {
                val deletedIdList = ArrayList<Long>()//已删除的歌曲id
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MUSIC_ID))
                    val path = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_PATH))
                    if (File(path).exists()) {
                        musicIds.add(id)
                    } else {
                        deletedIdList.add(id)
                    }
                }

                //删除歌曲表中不存在的歌曲
                if (deletedIdList.isNotEmpty()) {
                    val sb = StringBuilder()
                    for (i in deletedIdList.indices) {
                        sb.append(deletedIdList[i])
                        if (i < deletedIdList.lastIndex) {
                            sb.append(",")
                        }
                    }
                    val sql2 = "delete from $TABLE_MUSIC where $MUSIC_ID in ($sb)"
                    db?.execSQL(sql2)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db?.close()
        }
        return musicIds
    }

    fun getMostPair(): Pair<Long, Long> {
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val sql = "select $MUSIC_PATH,$ARTIST_ID,$ALBUM_ID" +
                    " from $TABLE_MUSIC" +
                    " order by $PLAY_TIMES desc"
            cursor = db!!.rawQuery(sql, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val path = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_PATH))
                    val artistId = cursor.getLong(cursor.getColumnIndexOrThrow(ARTIST_ID))
                    val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(ALBUM_ID))
                    if (File(path).exists()) {
                        return Pair(artistId, albumId)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db?.close()
        }
        return Pair(-1L, -1L)
    }
}