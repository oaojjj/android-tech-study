package com.example.mycustombrsender

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ResultReceiver : BroadcastReceiver() {
    companion object {
        const val MY_ACTION = "com.oseong.action.EXAMPLE_ACTION"
        const val MY_PERMISSION = "com.oseong.permission.EXAMPLE_ACTION_PERMISSION"
        const val EXTRA_KEY = "com.oseong.key.EXTRA_TEXT"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("ResultReceiver", "onReceive: ${intent?.action}")
        Thread.sleep(1000)
        val resultExtra = getResultExtras(true)
        val receivedText = resultExtra.getString(EXTRA_KEY) + " -> result receiver"
        resultExtra.putString(EXTRA_KEY, receivedText)

        Log.d("ResultReceiver", "onReceive: $receivedText")

        Toast.makeText(context, "result receiver", Toast.LENGTH_SHORT).show()
    }
}