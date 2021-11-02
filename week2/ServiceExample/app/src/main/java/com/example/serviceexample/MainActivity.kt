package com.example.serviceexample

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.serviceexample.databinding.ActivityMainBinding
import com.example.serviceexample.service.DownloadAction
import com.example.serviceexample.service.DownloadService
import java.io.File

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var downloadIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        downloadIntent = Intent(this, DownloadService::class.java)

        if (intent.hasExtra("id")) {
            val notiId = intent.getIntExtra("id", -1)

            with(downloadIntent) {
                putExtra("notificationId", notiId)
                action = DownloadAction.ACTION_CLICK_NOTIFICATION
                startService(this)
            }
        }

        binding.btStartService.setOnClickListener {
            if (binding.etImageUrl.text.isNotEmpty())
                with(downloadIntent) {
                    action = DownloadAction.ACTION_START_DOWNLOAD
                    putExtra("imageUrl", binding.etImageUrl.text.toString())
                    startService(this)
                }
        }
    }

    private fun setBitmapImage(intent:Intent) {
        val imagePath = intent.getStringExtra("image")
        val file = File(imagePath.toString())
        binding.ivDownloadedImage.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d("MainActivity", "onNewIntent:${intent?.action} ")

        if (intent?.action == DownloadAction.ACTION_COMPLETE_DOWNLOAD) setBitmapImage(intent)
        super.onNewIntent(intent)
    }

}