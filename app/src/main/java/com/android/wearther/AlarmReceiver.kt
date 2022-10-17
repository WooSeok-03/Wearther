package com.android.wearther

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class AlarmReceiver: BroadcastReceiver() {
    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent?) {
        // NotificationManager 생성
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val time = intent?.extras?.getString("time") ?: ""

        createChannel(context)
        sendNotification(context, time)
    }

    companion object {
        private const val NOTIFICATION_ID = 0
        private const val CHANNEL_ID = "channelID"
        private const val CHANNEL_NAME = "channelName"
    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableLights(true)    // 휴대폰 불빛
            channel.lightColor = Color.BLUE     // 휴대폰 불빛 색상
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(context: Context, time: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_wb_sunny_24)
            .setContentTitle(time)
            .setContentText("Hello World!")

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


}