package com.tool.mediadata.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
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
        const val MUSIC_PATH = "path"
        const val MUSIC_TITLE = "title"
        const val MUSIC_ARTIST = "artist"
        const val MUSIC_COVER = "cover"
        const val MUSIC_DURATION = "duration"

        fun getDownloadOpenHelper(context: Context?) = MusicOpenHelper(context, "download.db")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table IF NOT EXISTS $TABLE_MUSIC (" +
                "$MUSIC_PATH varchar(100) not null PRIMARY KEY," +
                "$MUSIC_TITLE varchar(100) not null," +
                "$MUSIC_ARTIST varchar(100) not null," +
                "$MUSIC_COVER varchar(100) not null," +
                "$MUSIC_DURATION integer not null" +
                ")"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private var db: SQLiteDatabase? = null

    fun insert(path: String, title: String, artist: String, cover: String, duration: Long) {
        delete(path)//path不能重复
        try {
            db = writableDatabase
            val sql = "insert into $TABLE_MUSIC values('$path','$title','$artist','$cover',$duration)"
            db?.execSQL(sql)
        } catch (e: Exception) {//已存在
            e.printStackTrace()
        } finally {
            db?.close()
        }
    }

    fun delete(path: String) {
        try {
            db = writableDatabase
            val sql = "delete from $TABLE_MUSIC where $MUSIC_PATH = '$path'"
            db?.execSQL(sql)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db?.close()
        }
    }

    fun getDataById(musicId: Long): Triple<String, String, String>? {
//        var cursor: Cursor? = null
//        try {
//            db = readableDatabase
//            val sql = "select $MUSIC_PATH,$MUSIC_TITLE,$MUSIC_ARTIST from $TABLE_MUSIC " +
//                    "where $MUSIC_ID = $musicId"
//            cursor = db!!.rawQuery(sql, null)
//            if (cursor != null && cursor.moveToFirst()) {
//                val path = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_PATH))
//                val title = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_TITLE))
//                val artist = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_ARTIST))
//                return Triple(path, title, artist)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.d("xxx", "getDataById: " + e)
//        } finally {
//            cursor?.close()
//            db?.close()
//        }
        return null
    }
}