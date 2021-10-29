package com.example.serviceexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.bt_start_service -> onStartService()
            R.id.bt_stop_service -> onStopService()
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
}