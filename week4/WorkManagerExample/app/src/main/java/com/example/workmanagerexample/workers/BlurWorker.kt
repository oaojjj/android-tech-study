package com.example.workmanagerexample.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanagerexample.Constants.KEY_IMAGE_URI
import com.example.workmanagerexample.R
import com.example.workmanagerexample.workers.WorkerUtils.blurBitmap
import com.example.workmanagerexample.workers.WorkerUtils.makeStatusNotification
import com.example.workmanagerexample.workers.WorkerUtils.writeBitmapToFile

class BlurWorker(var context: Context, params: WorkerParameters) : Worker(context, params) {


    override fun doWork(): Result {
        makeStatusNotification("이미지 블러처리중...", applicationContext)

        return try {
            val picture = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.test
            )

            val output = blurBitmap(picture, applicationContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(applicationContext, output)

            makeStatusNotification("결과물: $outputUri", applicationContext)

            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)
        } catch (throwable: Throwable) {
            Toast.makeText(context, "블러처리중 에러발생!", Toast.LENGTH_SHORT).show()

            Result.failure()
        }
    }
}