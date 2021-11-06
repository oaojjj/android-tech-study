package com.example.mycustombrsender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycustombrsender.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val MY_ACTION = "com.oseong.action.EXAMPLE_ACTION"
        const val MY_PERMISSION = "com.oseong.permission.EXAMPLE_ACTION_PERMISSION"
        const val EXTRA_KEY = "com.oseong.key.EXTRA_TEXT"
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btSent.setOnClickListener {
            Intent().let {
                it.action = MY_ACTION
                it.putExtra(EXTRA_KEY, binding.etText.text.toString())
                sendBroadcast(it, MY_PERMISSION)
            }
        }
    }
}