package com.example.mycustombrexample

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mycustombrexample.listener.OnReceiveBroadcastListener

class MainActivity : AppCompatActivity(), OnReceiveBroadcastListener {
    lateinit var customBRExample: CustomBRExample
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customBRExample =
            CustomBRExample().apply { setOnReceiveBroadcastListener(this@MainActivity) }
        val intentFilter = IntentFilter(CustomBRExample.MY_ACTION)
        registerReceiver(customBRExample, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(customBRExample)
    }

    override fun onReceivedBroadcast(receivedText: String?) {
        findViewById<TextView>(R.id.tv_contents).text = receivedText
    }
}