package com.kkobook.kkotimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)
        notifyNotification(context)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "타이머",
                NotificationManager.IMPORTANCE_HIGH
            )

            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }

    private fun notifyNotification(context: Context) = with(NotificationManagerCompat.from(context)) {
        val build = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Kkobook Timer")
            .setContentText("시간 종료!")
            .setSmallIcon(R.drawable.kkobook_eye_open)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)

        notify(NOTIFICATION_ID, build.build())
    }

    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"
        const val NOTIFICATION_ID = 1234
    }

}