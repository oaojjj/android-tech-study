package com.example.serviceexample.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import com.example.serviceexample.service.DownloadNotification.Companion.MAX_PROGRESS
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DownloadService", "onStartCommand: extraID:${intent?.getIntExtra("id", -1)}")
        val notiId = intent?.getIntExtra("id", startId) ?: startId

        Log.d("DownloadService", "onStartCommand: ${intent?.action}, $notiId")
        when (intent?.action) {
            DownloadAction.ACTION_START_DOWNLOAD -> {
                notis[notiId] = ServiceThread(notiId).apply { start() }
                startForegroundService(notiId)
            }
            DownloadAction.ACTION_PAUSE_DOWNLOAD ->
                notis[notiId]?.updateNotification(false)
            DownloadAction.ACTION_CONTINUE_DOWNLOAD ->
                notis[notiId]?.updateNotification(true)
            DownloadAction.ACTION_CANCEL_DOWNLOAD -> stopForegroundService(notiId)
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
    private fun startForegroundService(id: Int) {
        startForeground(id, notis[id]?.downloadNotification?.getNotification())
    }

    private fun stopForegroundService(id: Int) {
        Log.d("DownloadService", "stopForegroundService: $id ,${notis[id]}")
        removeNoti(id)
        stopForeground(true)
        stopSelf(id)
    }

    private fun removeNoti(id: Int) {
        notis[id]?.stopThread()
        notis.remove(id)
    }

    /**
     * worker thread
     * UI 및 다운로드 처리
     */
    inner class ServiceThread(id: Int) : Thread() {
        private var progressCurrent = 0
        var downloadNotification =
            DownloadNotification().apply { createNotificationBuilder(this@DownloadService, id) }

        override fun run() {
            while (progressCurrent <= MAX_PROGRESS) {
                Log.d("ServiceThread", "handleMessage:$progressCurrent")
                try {
                    downloadNotification.updateProgress(progressCurrent++)
                    sleep(1000)
                } catch (e: InterruptedException) {
                    cancelDownload()
                    break
                }
            }
            if (progressCurrent >= MAX_PROGRESS) completeDownload()
        }

        private fun completeDownload() {
            Log.d("DownloadService", "completeDownload")

            progressCurrent = 0
            downloadNotification.clearNotification("다운로드가 완료되었습니다.")
            showToast("다운로드가 완료되었습니다.")
        }

        private fun cancelDownload() {
            Log.d("DownloadService", "cancelDownload")
            showToast("다운로드를 취소했습니다.")
        }

        fun updateNotification(b: Boolean) = downloadNotification.updateNotificationAction(b)

        private fun showToast(text: String) = Handler(mainLooper).post {
            Toast.makeText(this@DownloadService, text, Toast.LENGTH_SHORT).show()
        }

        fun stopThread() {
            Log.d("DownloadService", "stopThread: ")
            downloadNotification.cancelNotification()
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