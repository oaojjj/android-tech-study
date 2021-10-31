package com.example.serviceexample.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import java.util.*

/**
 * 다운로드 서비스 예제는 IntentService를 상속하지 않는다.
 * 이유는 동시에 여러 작업 요청(멀티 스레딩)을 처라하기 위해서 Service를 상속받아 구현한다.
 */
class DownloadService : Service() {
    private var notis = mutableMapOf<Int, ServiceThread>()

    override fun onCreate() {
        Log.d("DownloadService", "onCreate")
        super.onCreate()
    }

    /**
     * Started Service
     * intent의 action에 따라 service를 처리할 수 있다.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notiId = intent?.getIntExtra("notificationId", startId) ?: startId
        Log.d("DownloadService", "onStartCommand: ${intent?.action}, $notiId")

        when (intent?.action) {
            DownloadAction.ACTION_START_DOWNLOAD -> startForegroundService(notiId)
            DownloadAction.ACTION_PAUSE_DOWNLOAD -> notis[notiId]?.updateNotification(false)
            DownloadAction.ACTION_CONTINUE_DOWNLOAD -> notis[notiId]?.updateNotification(true)
            DownloadAction.ACTION_CANCEL_DOWNLOAD -> stopForegroundService(notiId)
            DownloadAction.ACTION_CLICK_NOTIFICATION -> clickNotification(notiId)
        }

        return START_REDELIVER_INTENT
    }

    // click event
    private fun clickNotification(notiId: Int) {
        TODO("Not yet implemented")
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
    private fun startForegroundService(id: Int) {
        with(notis) {
            this[id] = ServiceThread(id).apply { start() }
            startForeground(id, this[id]!!.notification.getNotification())
        }
    }

    private fun stopForegroundService(id: Int) {
        Log.d("DownloadService", "stopForegroundService: $id")
        removeNotification(id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) stopForeground(STOP_FOREGROUND_DETACH)
        else stopForeground(true)

        stopSelf(id)
        if (notis.isEmpty()) stopSelf()
    }

    private fun removeNotification(id: Int) {
        notis[id]?.stopThread()
        notis.remove(id)
    }

    /**
     * worker thread
     * UI 및 다운로드 처리
     */
    inner class ServiceThread(var id: Int) : Thread() {
        var notification =
            DownloadNotification(id).apply { createNotificationBuilder(this@DownloadService) }

        // 일단 임시로 다운로드가 끝났는지 체크
        var isComplete = false

        override fun run() {
            while (!notification.isReachedMaxProgress()) {
                Log.d("ServiceThread", "handleMessage:${notification.getCurrentProgress()}")
                try {
                    notification.updateProgress()
                    sleep(1000)
                } catch (e: InterruptedException) {
                    break
                }
            }

            if (notification.isReachedMaxProgress()) completeDownload()
            else cancelDownload()
        }

        // 이미지 다운로드 완료
        private fun completeDownload() {
            Log.d("DownloadService", "completeDownload")

            isComplete = true
            notification.clearNotification("다운로드가 완료되었습니다.")
            showToast("다운로드가 완료되었습니다.")
            stopForegroundService(id)
        }

        // 이미지 다운로드 취소
        private fun cancelDownload() {
            Log.d("DownloadService", "cancelDownload")
            notification.clearNotification("다운로드가 취소되었습니다.")
            showToast("다운로드가 취소되었습니다.")
            stopForegroundService(id)
        }

        fun updateNotification(isDownloading: Boolean) =
            notification.changeNotificationAction(isDownloading)

        private fun showToast(text: String) = Handler(mainLooper).post {
            Toast.makeText(this@DownloadService, text, Toast.LENGTH_SHORT).show()
        }

        fun stopThread() {
            Log.d("DownloadService", "stopThread:$id ")
            interrupt()
        }
    }

    /**
     * Bound Service
     * 서비스와의 통신을 위해 Ibinder 인터페이스를 정의하는 binder를 통해 service를 반환할 수 있다.
     * 사용하지 않는다면 null을 반환한다.
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}