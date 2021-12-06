package com.example.workmanagerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.workmanagerexample.databinding.ActivityMainBinding
import com.example.workmanagerexample.workers.BlurWorker

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goButton.setOnClickListener {
            workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueue(OneTimeWorkRequest.from(BlurWorker::class.java))
        }
    }
}