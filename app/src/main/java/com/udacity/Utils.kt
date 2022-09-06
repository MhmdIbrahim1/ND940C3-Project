package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


fun NotificationManager.sendNotification(
    message_body: String,
    application_context: Context,
    file_name: String,
    download_status: Boolean)
{

    val intentContent = Intent(application_context, DetailActivity::class.java).apply {
        putExtra("fileName", file_name)
        putExtra("downloadStatus", download_status)
    }
    val pendingIntent = PendingIntent.getActivity(
        application_context,
        Constants.NOTIFICATION_ID,
        intentContent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val builder = NotificationCompat.Builder(
        application_context,
        application_context.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(application_context
            .getString(R.string.notification_title))
        .setContentText(message_body)
        .setContentIntent(pendingIntent)
        .addAction(R.drawable.ic_assistant_black_24dp, "Check the Status", pendingIntent)
        .setAutoCancel(true)

    notify(Constants.NOTIFICATION_ID, builder.build())
}