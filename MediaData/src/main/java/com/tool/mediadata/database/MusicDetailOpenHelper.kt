package com.tool.mediadata.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tool.mediadata.entity.MusicDetail

/**
 * desc:
 **
 * user: xujj
 * time: 2025/6/5 10:00
 **/
class MusicDetailOpenHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, VERSION_CODE) {

    companion object {
        private const val VERSION_CODE = 1
        private const val DB_NAME = "music_detail.db"

        const val TABLE_MUSIC_DETAIL = "music_detail"
        const val MUSIC_ID = "_id"
        const val MUSIC_NAME = "_name"
        const val MUSIC_ARTIST = "_artist"
        const val MUSIC_ALBUM = "_album"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table IF NOT EXISTS $TABLE_MUSIC_DETAIL (" +
                "$MUSIC_ID integer not null PRIMARY KEY," +
                "$MUSIC_NAME varchar(100) not null," +
                "$MUSIC_ARTIST varchar(100) not null," +
                "$MUSIC_ALBUM varchar(100) not null" +
                ")"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private var db: SQLiteDatabase? = null

    fun rename(
        id: Long,
        name: String,
        artist: String,
        album: String
    ) {
        rename(MusicDetail(id, name, artist, album))
    }

    fun rename(musicDetail: MusicDetail) {
        try {
            db = writableDatabase

            var isExist = false
            val sql1 =
                "select $MUSIC_ID from $TABLE_MUSIC_DETAIL where $MUSIC_ID = ${musicDetail.id}"
            db?.rawQuery(sql1, null)?.let {
                isExist = it.moveToFirst()
                it.close()
            }

            if (isExist) {
                val sql = "update $TABLE_MUSIC_DETAIL " +
                        "set $MUSIC_NAME = '${musicDetail.name}', " +
                        "$MUSIC_ARTIST = '${musicDetail.artist}', " +
                        "$MUSIC_ALBUM = '${musicDetail.album}' " +
                        "where $MUSIC_ID = ${musicDetail.id}"
                db?.execSQL(sql)
            } else {
                val sql = "insert into $TABLE_MUSIC_DETAIL " +
                        "values(${musicDetail.id}," +
                        "'${musicDetail.name}'," +
                        "'${musicDetail.artist}'," +
                        "'${musicDetail.album}')"
                db?.execSQL(sql)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db?.close()
        }
    }

    fun delete(musicId: Long) {
        try {
            db = writableDatabase
            val sql = "delete from $TABLE_MUSIC_DETAIL where $MUSIC_ID = $musicId"
            db?.execSQL(sql)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db?.close()
        }
    }

    fun query(): List<MusicDetail> {
        val musicDetails = ArrayList<MusicDetail>()
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val sql = "select $MUSIC_ID,$MUSIC_NAME,$MUSIC_ARTIST,$MUSIC_ALBUM" +
                    " from $TABLE_MUSIC_DETAIL"
            cursor = db!!.rawQuery(sql, null)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MUSIC_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_NAME))
                val artist = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_ARTIST))
                val album = cursor.getString(cursor.getColumnIndexOrThrow(MUSIC_ALBUM))
                musicDetails.add(MusicDetail(id, name, artist, album))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db?.close()
        }
        return musicDetails
    }
}