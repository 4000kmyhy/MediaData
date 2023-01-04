package com.xxx.mediadata.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri

/**
 * desc:
 **
 * user: xujj
 * time: 2022/8/25 16:56
 **/
class MediaScanner(context: Context?, path: String?, listener: OnMediaScannerListener?) {

    companion object {
        @JvmStatic
        fun scanFile(context: Context?, path: String?, listener: OnMediaScannerListener?) {
            MediaScanner(context, path, listener)
        }
    }

    interface OnMediaScannerListener {
        fun onScanCompleted(path: String?, uri: Uri?)
    }

    private var mConn: MediaScannerConnection? = null

    init {
        if (context != null) {
            mConn = MediaScannerConnection(
                context.applicationContext,
                object : MediaScannerConnectionClient {
                    override fun onMediaScannerConnected() {
                        try {
                            mConn?.scanFile(path, null)
                        } catch (e: Throwable) {//runCallBack空指针？
                            e.printStackTrace();
                        }
                    }

                    override fun onScanCompleted(path: String, uri: Uri) {
                        mConn?.disconnect()
                        mConn = null
                        listener?.onScanCompleted(path, uri)
                    }
                })
            mConn?.connect()
        }
    }
}