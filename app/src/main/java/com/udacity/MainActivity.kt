package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var fileName = ""
    private var downloadStatus = false


    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        // create channel
        channelCreator(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_title)
        )

        custom_button.setBtnState(ButtonState.Loading)

        custom_button.setOnClickListener {

            when {
                radio_btn_glide.isChecked -> {
                    fileName = radio_btn_glide.text.toString()
                    download(Constants.URL_GLIDE)
                }
                radio_btn_udacity.isChecked -> {
                    fileName = radio_btn_udacity.text.toString()
                    download(Constants.URL_UDACITY)
                }
                radio_btn_retrofit.isChecked -> {
                    fileName = radio_btn_retrofit.text.toString()
                    download(Constants.URL_RETROFIT)
                }
                else -> {
                    Toast.makeText(
                        applicationContext,
                        "No selected file!, Please select the file to download",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val channel = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
            if (channel.moveToFirst()) {
                val status: Int =
                    channel.getInt(channel.getColumnIndex(DownloadManager.COLUMN_STATUS))
                Log.i("statusDownload", status.toString())
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        downloadStatus = true
                    }
                    DownloadManager.STATUS_FAILED -> {
                        downloadStatus = false
                    }
                }

                sendNotification()
                channel.close()
            }
        }

    }

    private fun sendNotification() {
        notificationManager.sendNotification(
            getString(R.string.notification_description),
            applicationContext,
            fileName,
            downloadStatus
        )
        custom_button.setBtnState(ButtonState.Completed)
    }

    private fun download(url: String) {
        custom_button.setBtnState(ButtonState.Clicked)

        val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
    }


    private fun channelCreator(channel: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a unique name for the notification channel
            val notificationChannel = NotificationChannel(
                channel,
                channelName,
                // Change importance
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)

            notificationManager = getSystemService(NotificationManager::class.java)
            // Create the channel using notificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}
