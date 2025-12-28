package com.onesecond.daily

import android.content.Context
import android.media.MediaScannerConnection
import java.io.File

object MediaScannerHelper {
    fun scanFile(context: Context, file: File) {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            arrayOf("video/mp4"),
            null
        )
    }
}