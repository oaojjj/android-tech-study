package com.example.mycustombrsender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycustombrsender.ResultReceiver.Companion.EXTRA_KEY
import com.example.mycustombrsender.ResultReceiver.Companion.MY_ACTION
import com.example.mycustombrsender.ResultReceiver.Companion.MY_PERMISSION
import com.example.mycustombrsender.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btSent.setOnClickListener {
            Intent().let {
                it.action = MY_ACTION
                it.putExtra(EXTRA_KEY, binding.etText.text.toString())
                // packageName 또는 componentName을 명시해주면 manifest에 등록해도 명시적 인텐트가 된다.
                it.`package` = "com.example.mycustombrexample"
                sendOrderedBroadcast(
                    it,
                    MY_PERMISSION,
                    ResultReceiver(),
                    null,
                    0,
                    "Start",
                    Bundle().apply { putString(EXTRA_KEY, "Start") }
                )
            }
        }
    }
}