package com.example.serviceexample.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Toast
import com.example.serviceexample.MainActivity
import java.io.BufferedInputStream
import java.net.URL
import java.util.*
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat


/**
 * 다운로드 서비스 예제는 IntentService를 상속하지 않는다.
 * 이유는 동시에 여러 작업 요청(멀티 스레딩)을 처라하기 위해서 Service를 상속받아 구현한다.
 */
class DownloadService : Service() {
    private var threads = mutableMapOf<Int, ServiceThread>()

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
        val imageUrl = intent?.getStringExtra("imageUrl") ?: "null"
        Log.d("DownloadService", "onStartCommand: ${intent?.action}, $notiId")

        when (intent?.action) {
            DownloadAction.ACTION_START_DOWNLOAD -> startForegroundService(notiId, imageUrl)
            DownloadAction.ACTION_PAUSE_DOWNLOAD -> threads[notiId]?.updateNotification(false)
            DownloadAction.ACTION_CONTINUE_DOWNLOAD -> threads[notiId]?.updateNotification(true)
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
    private fun startForegroundService(id: Int, imageUrl: String) {
        with(threads) {
            this[id] = ServiceThread(id, imageUrl).apply { start() }
            startForeground(id, this[id]!!.notification.getNotification())
        }
    }

    private fun stopForegroundService(id: Int) {
        Log.d("DownloadService", "stopForegroundService: $id")
        removeNotification(id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) stopForeground(STOP_FOREGROUND_REMOVE)
        else stopForeground(true)

        stopSelf(id)
        if (threads.isEmpty()) stopSelf()
    }

    private fun removeNotification(id: Int) {
        threads[id]?.notification!!.removeNotification()
        threads.remove(id)
    }

    /**
     * worker thread
     * UI 및 다운로드 처리
     */
    inner class ServiceThread(var id: Int, var imageUrl: String) : Thread() {
        var notification =
            DownloadNotification(id).apply { createNotificationBuilder(this@DownloadService) }
        private val savePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()
        private var localPath = savePath

        override fun run() {
            Log.d("DownloadThread", "run: $savePath")
            downloading()
            if (notification.isReachedMaxProgress()) completeDownload()
            else cancelDownload()
        }


        // 이미지 다운로드
        private fun downloading() {
            Log.d("DownloadThread", "downloading: $imageUrl")
            // save path
            val fileName =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date()).toString()
            localPath = "$savePath/$fileName.jpg"
            val file = File(localPath)

            // read, write stream
            val url = URL(imageUrl)
            val connection = url.openConnection().apply { connect() }

            val inputStream = BufferedInputStream(url.openStream())
            val outputStream = FileOutputStream(file)
            val data = ByteArray(1024)
            var total: Long = 0
            var count: Int

            while (inputStream.read(data)
                    .also { count = it } != -1 || !notification.isReachedMaxProgress()
            ) {
                try {
                    Log.d(
                        "DownloadThread",
                        "downloading: $total ,${(total * 100 / connection.contentLength)}"
                    )
                    outputStream.write(data, 0, count)

                    total += count.toLong()
                    notification.updateProgress((total * 100 / connection.contentLength).toInt())
                } catch (e: InterruptedException) {
                    break
                }
            }
            inputStream.close()
            outputStream.close()

            Log.d("DownloadThread", "downloading: $file")
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED, Uri.fromFile(file)))
        }


        // 이미지 다운로드 완료
        private fun completeDownload() {
            Log.d("DownloadThread", "completeDownload")

            notification.clearNotification("다운로드가 완료되었습니다.")
            showToast("다운로드가 완료되었습니다.")
            stopForegroundService(id)
            startActivity(Intent(applicationContext, MainActivity::class.java).apply {
                action = DownloadAction.ACTION_COMPLETE_DOWNLOAD
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("image", localPath)
            })
        }

        // 이미지 다운로드 취소
        private fun cancelDownload() {
            Log.d("DownloadThread", "cancelDownload")

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
            Log.d("DownloadThread", "stopThread:$id ")
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