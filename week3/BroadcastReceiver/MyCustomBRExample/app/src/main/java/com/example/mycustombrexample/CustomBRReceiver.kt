package com.example.mycustombrexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.mycustombrexample.MainActivity.Companion.EXTRA_KEY
import com.example.mycustombrexample.MainActivity.Companion.MY_ACTION
import com.example.mycustombrexample.listener.OnReceiveBroadcastListener

class CustomBRReceiver : BroadcastReceiver() {

    private lateinit var mListener: OnReceiveBroadcastListener

    fun setOnReceiveBroadcastListener(listener: OnReceiveBroadcastListener) {
        mListener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(MY_ACTION)) {
            Thread.sleep(1000)
            val resultExtra = getResultExtras(true)
            val receivedText = resultExtra.getString(EXTRA_KEY) + " -> receiver "
            resultExtra.putString(EXTRA_KEY, receivedText)

            //Toast.makeText(context, "received broadcast", Toast.LENGTH_SHORT).show()
            Log.d("CustomBRReceiver", "onReceive: $receivedText")

            // mListener.onReceivedBroadcast(receivedText)

            // 최종 전달/반환 값
            setResult(resultCode, "receiver", resultExtra)
        }
    }
}