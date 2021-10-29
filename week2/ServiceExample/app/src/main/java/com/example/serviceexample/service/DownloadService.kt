package com.example.serviceexample.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

/**
 * Service는 별도로 Thread를 동작하지 않는 이상 기본적으로 UI Thread에서 동작한다.
 * 백그라운드에서 최대 15분 정도 실행될 수 있고, 메모리가 부족하거나 시간을 초과할 경우 강제로 종료될 수 있다.
 * 계속 실행하게 하려면 포그라운드 서비스를 사용해야한다.
 */
class DownloadService : Service() {
    private val mBinder = MyBinder()
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
                    }
                    DownloadAction.ACTION_FINISHED_DOWNLOAD -> {
                        // do something..
                    }
                }
            }
        }
        mThread!!.start()
        return START_REDELIVER_INTENT
    }


    override fun onDestroy() {
        Log.d("DownloadService", "onDestroy")
        stopThread()
        super.onDestroy()
    }

    private fun startDownload() {
        for (i in 0..100) {
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
     */
    inner class MyBinder : Binder() {
        fun getService(): DownloadService {
            return this@DownloadService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

}