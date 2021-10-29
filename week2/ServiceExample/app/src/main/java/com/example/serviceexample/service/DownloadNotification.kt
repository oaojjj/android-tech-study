package com.example.serviceexample.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.serviceexample.MainActivity
import com.example.serviceexample.R

object DownloadNotification {
    private const val CHANNEL_ID = "DOWNLOAD_SERVICE"
    private const val INTENT_REQUEST_CODE = 0

    const val MAX_PROGRESS = 3

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var pauseDownloadIntent: PendingIntent
    private lateinit var continueDownloadIntent: PendingIntent
    private lateinit var cancelDownloadIntent: PendingIntent

    private fun initIntent(context: Context) {
        pendingIntent = Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(context, INTENT_REQUEST_CODE, it, 0)
        }

        pauseDownloadIntent =
            createServiceIntent(context, DownloadAction.ACTION_PAUSE_DOWNLOAD)
        continueDownloadIntent =
            createServiceIntent(context, DownloadAction.ACTION_CONTINUE_DOWNLOAD)
        cancelDownloadIntent =
            createServiceIntent(context, DownloadAction.ACTION_CANCEL_DOWNLOAD)
    }

    private fun createServiceIntent(context: Context, action: String) =
        Intent(context, DownloadService::class.java).let {
            it.action = action
            PendingIntent.getService(context, INTENT_REQUEST_CODE, it, 0)
        }

    /**
     * Notification Channel
     * 오레오 버전 이후로는 채널을 꼭 만들어야 한다.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "DOWNLOAD_SERVICE",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun createNotificationBuilder(context: Context): NotificationCompat.Builder {
        initIntent(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(context)
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Download")
            .setContentText("downloading..")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setProgress(MAX_PROGRESS, 0, false)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.pause_download, "pause", pauseDownloadIntent)
            .addAction(R.drawable.ic_baseline_close_24, "cancel", cancelDownloadIntent)
    }

    fun notify(builder: NotificationCompat.Builder) {
        notificationManager.notify(DownloadService.NOTIFICATION_ID, builder.build())
    }

    fun updateNotification(isDownloading: Boolean, builder: NotificationCompat.Builder) {
        builder.clearActions()
        if (isDownloading)
            builder.addAction(R.drawable.pause_download, "pause", pauseDownloadIntent)
        else
            builder.addAction(R.drawable.continue_download, "continue", continueDownloadIntent)

        builder.addAction(R.drawable.ic_baseline_close_24, "cancel", cancelDownloadIntent)

        notificationManager.notify(DownloadService.NOTIFICATION_ID, builder.build())
    }

}