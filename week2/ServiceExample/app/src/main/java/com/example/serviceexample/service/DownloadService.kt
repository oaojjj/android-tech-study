package com.example.serviceexample.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.serviceexample.service.DownloadNotification.MAX_PROGRESS

/**
 * 다운로드 서비스 예제는 IntentService를 상속하지 않는다.
 * 이유는 동시에 여러 작업 요청(멀티 스레딩)을 처라하기 위해서 Service를 상속받아 구현한다.
 */
class DownloadService : Service() {
    companion object {
        const val NOTIFICATION_ID = 7
    }

    private lateinit var handlerThread: HandlerThread
    private lateinit var notificationBuilder: NotificationCompat.Builder

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        private var progressCurrent = 0
        override fun handleMessage(msg: Message) {
            while (progressCurrent <= MAX_PROGRESS) {
                Log.d("DownloadService", "onStartCommand:$progressCurrent")
                try {
                    notificationBuilder.setProgress(MAX_PROGRESS, progressCurrent++, false)
                        .let { DownloadNotification.notify(it) }
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                }
            }
            if (progressCurrent >= MAX_PROGRESS) completeDownload()
            else if (progressCurrent < MAX_PROGRESS) cancelDownload()
        }

        private fun cancelDownload() {
            Toast.makeText(this@DownloadService, "다운로드를 취소했습니다.", Toast.LENGTH_SHORT).show()
        }

        private fun completeDownload() {
            Log.d("DownloadService", "onDownloadFinished")

            progressCurrent = 0
            notificationBuilder.setContentText("Download complete")
                .let { DownloadNotification.notify(it) }
            Toast.makeText(this@DownloadService, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate() {
        Log.d("DownloadService", "onCreate")
        handlerThread =
            HandlerThread("DownloadStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
                start()
                serviceLooper = looper
                serviceHandler = ServiceHandler(looper)
            }
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DownloadService", "onStartCommand: ${intent?.action}")
        when (intent?.action) {
            DownloadAction.ACTION_START_DOWNLOAD -> {
                startForegroundService()
                serviceHandler?.obtainMessage()?.also { msg ->
                    msg.arg1 = startId
                    serviceHandler?.sendMessage(msg)
                }
            }
            DownloadAction.ACTION_PAUSE_DOWNLOAD -> {
                // do something..
            }
            DownloadAction.ACTION_CONTINUE_DOWNLOAD -> {
            }
            DownloadAction.ACTION_CANCEL_DOWNLOAD -> stopForegroundService()
        }

        return START_STICKY
    }


    override fun onDestroy() {
        Log.d("DownloadService", "onDestroy")
        super.onDestroy()
    }


    /**
     * Foreground Service
     * 다른 component에서 startForegroundService(intent)를 호출하고
     * 5초이내에 startForeground(id, notification)이 응답하지 않을 경우 ANR이 발생한다.
     */
    private fun startForegroundService() {
        notificationBuilder = DownloadNotification.createNotificationBuilder(this)
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        handlerThread.interrupt()
        serviceHandler?.removeCallbacksAndMessages(null)
        stopForeground(true)
        stopSelf()
    }

    /**
     * Bound Service
     * 서비스와의 통신을 위해 Ibinder 인터페이스를 정의하는 binder를 통해 service를 반환할 수 있다.
     * 사용하지 않느다면 null을 반환한다.
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}