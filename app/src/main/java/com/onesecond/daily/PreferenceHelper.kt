package com.onesecond.daily

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object PreferenceHelper {
    private const val PREFS_NAME = "OneSecondDailyPrefs"
    private const val KEY_LAST_RECORDED_DATE = "last_recorded_date"
    private const val KEY_VIDEO_PATH = "video_path_"

    fun markTodayAsRecorded(context: Context, videoPath: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today = getCurrentDate()
        prefs.edit()
            .putString(KEY_LAST_RECORDED_DATE, today)
            .putString(KEY_VIDEO_PATH + today, videoPath)
            .apply()
    }

    fun hasRecordedToday(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastRecorded = prefs.getString(KEY_LAST_RECORDED_DATE, "")
        val today = getCurrentDate()
        return lastRecorded == today
    }

    fun getTodayVideoPath(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today = getCurrentDate()
        return prefs.getString(KEY_VIDEO_PATH + today, null)
    }

    fun clearTodayRecord(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today = getCurrentDate()
        prefs.edit()
            .remove(KEY_LAST_RECORDED_DATE)
            .remove(KEY_VIDEO_PATH + today)
            .apply()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
        return dateFormat.format(Date())
    }
}