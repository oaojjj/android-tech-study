package com.example.jobschedulerexample

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.example.jobschedulerexample.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        private const val JOB_ID = 7
        private const val TAG = "MainActivity"
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(this, MyJobService::class.java)
        val jobInfo = JobInfo.Builder(JOB_ID, componentName)
            .setRequiresCharging(true)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED )
            .setExtras(PersistableBundle().apply { putString("EXTRA_KEY", "JobSchedulerExample") })
            .build()

        binding.btnStart.setOnClickListener {
            Log.d(TAG, "onCreate: JobScheduler Start $JOB_ID")
            jobScheduler.schedule(jobInfo)
        }

        binding.btnStop.setOnClickListener {
            Log.d(TAG, "onCreate: JobScheduler Stop $JOB_ID")
            jobScheduler.cancel(JOB_ID)
        }
    }
}