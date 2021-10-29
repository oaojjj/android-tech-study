package com.example.serviceexample.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * 다운로드 서비스 예제는 IntentService를 상속하지 않는다.
 * 이유는 동시에 여러 작업 요청(멀티 스레딩)을 처라하기 위해서 Service를 상속받아 구현한다.
 */
class DownloadService : Service() {
    private var mThread: Thread? = null

    override fun onCreate() {
        Log.d("DownloadService", "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DownloadService", "onStartCommand")
        if (mThread == null) {
            mThread = Thread {
                when (intent?.action) {
                    DownloadAction.ACTION_START_DOWNLOAD -> startDownload()
                    DownloadAction.ACTION_STOP_DOWNLOAD -> {
                        // do something..
                        stopSelf()
                    }
                    DownloadAction.ACTION_FINISHED_DOWNLOAD -> {
                        // do something..
                    }
                }
            }
            mThread?.start()
        }
        return START_STICKY
    }


    override fun onDestroy() {
        Log.d("DownloadService", "onDestroy")
        stopThread()
        super.onDestroy()
    }

    private fun startDownload() {
        for (i in 0..5) {
            try {
                Thread.sleep(1000)
                Log.d("DownloadService", "onStartCommand: $i")
            } catch (e: InterruptedException) {
                break
            }
        }
    }

    private fun stopThread() {
        mThread?.interrupt()
        mThread = null
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