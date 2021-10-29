package com.example.serviceexample.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

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