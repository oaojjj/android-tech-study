package com.example.mycustombrexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.mycustombrexample.MainActivity.Companion.EXTRA_KEY
import com.example.mycustombrexample.MainActivity.Companion.MY_ACTION
import com.example.mycustombrexample.listener.OnReceiveBroadcastListener

class CustomBRReceiver3 : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("CustomBRReceiver3", "onReceive: ${intent?.action}")
        if (intent?.action.equals(MY_ACTION)) {
            Thread.sleep(1000)
            val resultExtra = getResultExtras(true)
            val receivedText = resultExtra.getString(EXTRA_KEY) + " -> receiver3"
            resultExtra.putString(EXTRA_KEY, receivedText)

            //Toast.makeText(context, "received broadcast3", Toast.LENGTH_SHORT).show()
            Log.d("CustomBRReceiver3", "onReceive: $receivedText")

            // 최종 전달/반환 값
            setResult(resultCode, "receiver3", resultExtra)
        }
    }
}