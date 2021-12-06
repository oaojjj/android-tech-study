package com.example.serializablereceiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.model.ParcelableData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serializableKey = "serializableKey"
        val parcelableKey = "parcelableKey"

        val serializableData = intent.getSerializableExtra(serializableKey)
        val parcelableData = intent.getParcelableExtra<ParcelableData>(parcelableKey)

        findViewById<TextView>(R.id.tv_serializable_data).text = serializableData.toString()
        findViewById<TextView>(R.id.tv_parcelable_data).text = parcelableData.toString()
    }
}