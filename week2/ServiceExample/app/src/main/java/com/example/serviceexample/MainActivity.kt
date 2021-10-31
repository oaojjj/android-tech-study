package com.example.serviceexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.serviceexample.databinding.ActivityMainBinding
import com.example.serviceexample.service.DownloadAction
import com.example.serviceexample.service.DownloadService

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var downloadIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        downloadIntent = Intent(this, DownloadService::class.java)

        if (intent.hasExtra("id")) {
            val notiId = intent.getIntExtra("id", -1)
            Log.d("MainActivity", "onCreate: $notiId")

            with(downloadIntent) {
                putExtra("notificationId", notiId)
                action = DownloadAction.ACTION_CLICK_NOTIFICATION
                startService(this)
            }
        }

        binding.btStartService.setOnClickListener {
            with(downloadIntent) {
                action = DownloadAction.ACTION_START_DOWNLOAD
                startService(this)
            }
        }
    }

}