package com.example.mycustombrexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.mycustombrexample.listener.OnReceiveBroadcastListener

class CustomBRReceiver3 : BroadcastReceiver() {
    companion object {
        const val MY_ACTION = "com.oseong.action.EXAMPLE_ACTION"
        const val EXTRA_KEY = "com.oseong.key.EXTRA_TEXT"
    }

    private lateinit var mListener: OnReceiveBroadcastListener

    fun setOnReceiveBroadcastListener(listener: OnReceiveBroadcastListener) {
        mListener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(MY_ACTION)) {
            val receivedText = intent?.getStringExtra(EXTRA_KEY)
            Toast.makeText(context, "received broadcast", Toast.LENGTH_SHORT).show()
            mListener.onReceivedBroadcast(receivedText)
        }
    }
}