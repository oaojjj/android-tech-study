package com.example.serializablesender

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.model.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sendData(view: android.view.View) {
        val serializableKey = "serializableKey"

        val intent = Intent().apply {
            component = ComponentName(
                "com.example.serializablereceiver",
                "com.example.serializablereceiver.MainActivity"
            )
        }

        intent.putExtra(serializableKey, User("osg", 26, "hi~ my nickname is osg"))
        startActivity(intent)
    }
}