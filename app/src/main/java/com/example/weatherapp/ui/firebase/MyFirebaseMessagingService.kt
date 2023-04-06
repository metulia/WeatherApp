package com.example.weatherapp.ui.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.weatherapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (!message.data.isNullOrEmpty()) {
            val title = message.data[KEY_TITLE]
            val message = message.data[KEY_MESSAGE]
            if (!title.isNullOrEmpty() && !message.isNullOrEmpty()) {
                push(title, message)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun push(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderHigh =
            NotificationCompat.Builder(this, CHANNEL_ID_HIGH).apply {
                setSmallIcon(R.drawable.ic_rus)
                setContentTitle(title)
                setContentText(message)
                priority = NotificationManager.IMPORTANCE_HIGH
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameHigh = "Name $CHANNEL_ID_HIGH"
            val channelDescriptionHigh = "Description $CHANNEL_ID_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh =
                NotificationChannel(
                    CHANNEL_ID_HIGH,
                    channelNameHigh,
                    channelPriorityHigh
                ).apply {
                    description = channelDescriptionHigh
                }
            notificationManager.createNotificationChannel(channelHigh)
        }

        notificationManager.notify(
            NOTIFICATION_ID_HIGH,
            notificationBuilderHigh.build()
        )

    }

    companion object {

        private const val NOTIFICATION_ID_HIGH = 2
        private const val CHANNEL_ID_HIGH = "channel_id_high"
        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"
    }
}