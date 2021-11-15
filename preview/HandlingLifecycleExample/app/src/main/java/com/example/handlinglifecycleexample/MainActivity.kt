package com.example.handlinglifecycleexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// AppCompatActivity는 ComponentActivity를 상속하고 있으므로 자체적인 lifecycle을 가지고 있다.
class MainActivity : AppCompatActivity() {
    private var observer = MyObserver(this, lifecycle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // add observer
        lifecycle.addObserver(observer)
    }
}