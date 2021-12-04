package com.example.serializablereceiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serializableKey = "serializableKey"
        val user = intent.getSerializableExtra(serializableKey)

        findViewById<TextView>(R.id.tv_receive).text = user.toString()
    }
}