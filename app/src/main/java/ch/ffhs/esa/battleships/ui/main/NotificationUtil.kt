package ch.ffhs.esa.battleships.ui.main

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ch.ffhs.esa.battleships.R


class NotificationUtil {

    fun createNotification(activity: Activity, title: String, text: String) {
        val CHANNEL_ID = System.currentTimeMillis().toInt().toString()
        createNotificationChannel(activity, CHANNEL_ID)

        var builder = NotificationCompat.Builder(activity, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_adjust_24)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(activity)) {
            notify(0, builder.build())
        }
    }

    private fun createNotificationChannel(activity: Activity, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = activity.getString(R.string.channelName)
            val descriptionText = activity.getString(R.string.channelDesc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}