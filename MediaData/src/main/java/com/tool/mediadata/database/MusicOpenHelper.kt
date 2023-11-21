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
class MusicOpenHelper(context: Context?, dbName: String) :
    SQLiteOpenHelper(context, dbName, null, VERSION_CODE) {

    companion object {

        private const val VERSION_CODE = 1

        const val TABLE_MUSIC = "music"
        const val MUSIC_ID = "id"
        const val MUSIC_PATH = "path"

        fun getDownloadOpenHelper(context: Context?) = MusicOpenHelper(context, "download.db")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table IF NOT EXISTS $TABLE_MUSIC (" +
                "$MUSIC_ID integer not null PRIMARY KEY," +
                "$MUSIC_PATH varchar(100) not null" +
                ")"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private var db: SQLiteDatabase? = null

    fun insert(musicId: Long, path: String) {
        delete(musicId, path)
        try {
            db = writableDatabase
            val sql = "insert into $TABLE_MUSIC values($musicId,'$path')"
            db?.execSQL(sql)
        } catch (e: Exception) {//已存在
            e.printStackTrace()
        } finally {
            db?.close()
        }
    }

    fun delete(musicId: Long, path: String) {
        try {
            db = writableDatabase
            val sql = "delete from $TABLE_MUSIC where $MUSIC_ID = $musicId or $MUSIC_PATH = '$path'"
            db?.execSQL(sql)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db?.close()
        }
    }

    fun queryMusicIds(): MutableList<Long> {
        val musicIds = ArrayList<Long>()
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val sql = "select $MUSIC_ID,$MUSIC_PATH from $TABLE_MUSIC"
            cursor = db!!.rawQuery(sql, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val musicId = cursor.getLong(cursor.getColumnIndexOrThrow(MUSIC_ID))
                    val path = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_PATH))
                    if (!path.isNullOrEmpty() && File(path).exists()) {
                        if (!musicIds.contains(musicId)) {
                            musicIds.add(musicId)
                        }
                    }
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

    fun query(): MutableList<Pair<Long, String>> {
        val pairs = ArrayList<Pair<Long, String>>()
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val sql = "select $MUSIC_ID,$MUSIC_PATH from $TABLE_MUSIC"
            cursor = db!!.rawQuery(sql, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val musicId = cursor.getLong(cursor.getColumnIndexOrThrow(MUSIC_ID))
                    val path = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_PATH))
                    if (!path.isNullOrEmpty() && File(path).exists()) {
                        pairs.add(Pair(musicId, path))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db?.close()
        }
        return pairs
    }
}