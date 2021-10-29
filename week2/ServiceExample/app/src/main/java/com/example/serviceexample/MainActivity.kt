package com.example.serviceexample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import com.example.serviceexample.databinding.ActivityMainBinding
import com.example.serviceexample.service.DownloadAction
import com.example.serviceexample.service.DownloadService

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var downloadIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        downloadIntent = Intent(this, DownloadService::class.java)

        binding.btStartService.setOnClickListener(this)
        binding.btStopService.setOnClickListener(this)
        binding.btStartBound.setOnClickListener(this)
        binding.btStopBound.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.bt_start_service -> onStartService()
            R.id.bt_stop_service -> onStopService()
            R.id.bt_start_bound -> onBindService()
            R.id.bt_stop_bound -> {
            }
        }
    }

    /**
     * Started Service
     * intent의 action에 따라 service를 처리할 수 있다.
     */

    private fun onStartService() {
        Log.d("MainActivity", "onStartDownload")
        downloadIntent.action = DownloadAction.ACTION_START_DOWNLOAD
        startService(downloadIntent)
    }

    private fun onStopService() {
        Log.d("MainActivity", "onStopDownload")
        stopService(downloadIntent)
    }

    /**
     * Bound Service
     * 다른 컴포넌트(예제는 Activity)와 bind하여 참조를 통해 서비스 조작이 가능하다.
     */

    // bind 되었을 시 서비스와의 통신을 위한 참조변수
    private var downloadService: DownloadService? = null

    // service state
    private var isService = false

    private val connection = object : ServiceConnection {
        // bind 되면 호출되는 콜백
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isService = true
            downloadService = (service as DownloadService.MyBinder).getService()
            Log.d("MainActivity", "onServiceDisconnected: $isService")
        }

        // unbind 되었다고 호출되지 않고, 비정상적으로 종료되면 호출되는 콜백
        override fun onServiceDisconnected(name: ComponentName?) {
            isService = false
            Log.d("MainActivity", "onServiceDisconnected: $isService")
        }
    }

    private fun onBindService() {
        bindService(downloadIntent, connection, Context.BIND_AUTO_CREATE)
    }


}