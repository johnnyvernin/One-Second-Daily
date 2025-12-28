package com.onesecond.daily

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.*

object NotificationHelper {
    private const val CHANNEL_ID = "daily_reminder"
    private const val CHANNEL_NAME = "Lembretes Diários"

    private const val MORNING_REQUEST_CODE = 1001
    private const val AFTERNOON_REQUEST_CODE = 1002
    private const val EVENING_REQUEST_CODE = 1003

    fun scheduleNotifications(context: Context) {
        createNotificationChannel(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Manhã - 9h
        scheduleNotification(context, alarmManager, 9, 0, MORNING_REQUEST_CODE)

        // Tarde - 15h
        scheduleNotification(context, alarmManager, 15, 0, AFTERNOON_REQUEST_CODE)

        // Noite - 20h
        scheduleNotification(context, alarmManager, 20, 0, EVENING_REQUEST_CODE)
    }

    private fun scheduleNotification(
        context: Context,
        alarmManager: AlarmManager,
        hour: Int,
        minute: Int,
        requestCode: Int
    ) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            // Se já passou do horário hoje, agendar para amanhã
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Lembretes para gravar seu vídeo diário"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}