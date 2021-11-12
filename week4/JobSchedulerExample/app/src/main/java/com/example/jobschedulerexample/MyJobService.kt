package com.example.jobschedulerexample

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

/**
 * 기본적으로 Main Thread 에서 작동한다.
 * JobService는 Service를 상속하지만, onStartCommand()는 호출하지 않는다.
 */
class MyJobService : JobService() {
    companion object {
        private const val TAG = "MyJobService"
    }

    private var thread: Thread? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    /**
     * @return true - job is being running, false - work has been done
     */
    override fun onStartJob(params: JobParameters?): Boolean {
        val extra = params?.extras?.getString("EXTRA_KEY")
        Log.d(TAG, "onStartJob: $extra")
        doLongTask(params)
        return true
    }

    private fun doLongTask(params: JobParameters?) {
        var progress = 0

        // do something background task
        // if job finished, call jobFinished() method
        thread = Thread {
            while (progress++ < 10) {
                try {
                    Thread.sleep(1000)
                    Log.d(TAG, "doLongTask: $progress")

                } catch (e: InterruptedException) {
                    break
                }
            }

            when (progress) {
                10 -> {
                    Log.d(TAG, "doLongTask: job is done")
                    jobFinished(params, false)
                }
                else -> {
                    Log.d(TAG, "doLongTask: job is stopped")
                    jobFinished(params, true)
                }
            }
        }

        thread?.start()
    }


    /**
     * @return true - re_schedule job, false - stop job
     */
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob")
        thread?.interrupt()
        return true
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }
}