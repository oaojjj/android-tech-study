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

class DownloadNotification {
    companion object {
        private const val CHANNEL_ID = "DOWNLOAD_SERVICE"
        var INTENT_REQUEST_CODE = -1
        const val MAX_PROGRESS = 100
    }

    private var notiID: Int = 0
    lateinit var builder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var pauseDownloadIntent: PendingIntent
    private lateinit var continueDownloadIntent: PendingIntent
    private lateinit var cancelDownloadIntent: PendingIntent

    private fun initIntent(context: Context) {
        INTENT_REQUEST_CODE++
        pendingIntent = Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(
                context,
                INTENT_REQUEST_CODE,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
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
            it.putExtra("id", notiID)
            PendingIntent.getService(
                context,
                INTENT_REQUEST_CODE,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
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

    fun createNotificationBuilder(context: Context, id: Int): NotificationCompat.Builder {
        notiID = id
        initIntent(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(context)
        builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Download")
            .setContentText("downloading..")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setProgress(MAX_PROGRESS, 0, false)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.pause_download, "pause", pauseDownloadIntent)
            .addAction(R.drawable.ic_baseline_close_24, "cancel", cancelDownloadIntent)
        return builder
    }

    fun updateNotificationAction(isDownloading: Boolean): DownloadNotification {
        builder.clearActions()
        if (isDownloading)
            builder.addAction(R.drawable.pause_download, "pause", pauseDownloadIntent)
        else
            builder.addAction(R.drawable.continue_download, "continue", continueDownloadIntent)

        builder.addAction(R.drawable.ic_baseline_close_24, "cancel", cancelDownloadIntent)
        return this
    }

    fun updateProgress(value: Int) {
        builder.setProgress(MAX_PROGRESS, value, false)
        notificationManager.notify(notiID, builder.build())
    }

    fun clearNotification(text: String) {
        builder.setContentText(text)
            .clearActions()
            .setProgress(0, 0, false)
        notificationManager.notify(notiID, builder.build())
    }

    fun cancelNotification() {
        notificationManager.cancel(notiID)
    }

    fun getNotification() = builder.build()
}