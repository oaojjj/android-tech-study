package com.example.mycustombrexample

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mycustombrexample.listener.OnReceiveBroadcastListener

class MainActivity : AppCompatActivity(), OnReceiveBroadcastListener {
    companion object {
        const val MY_ACTION = "com.oseong.action.EXAMPLE_ACTION"
        const val EXTRA_KEY = "com.oseong.key.EXTRA_TEXT"
    }

    lateinit var customBRReceiver: CustomBRReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customBRReceiver =
            CustomBRReceiver().apply { setOnReceiveBroadcastListener(this@MainActivity) }
        val intentFilter = IntentFilter(MY_ACTION).apply { priority = 1 }
        registerReceiver(customBRReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(customBRReceiver)
    }

    override fun onReceivedBroadcast(receivedText: String?) {
        findViewById<TextView>(R.id.tv_contents).text = receivedText
    }
}