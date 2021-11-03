package com.example.broadcastreceiverexample

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.broadcastreceiverexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btBatteryRemainder.setOnClickListener {
            Toast.makeText(this, "배터리 잔량: ${getBatteryRemainder()}%", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {

        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    /**
     *  배터리 잔량
     *  BroadcastManager의 고정 인텐트를 사용하기 때문에
     *  브로드캐스트 등록할 필요 없음
     */
    private fun getBatteryRemainder(): Int {
        val batteryStatus = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        Log.d(TAG, "getBatteryRemainder: level=$level, scale=$scale")

        return level?.times(100)?.div(scale ?: -1) ?: -1
    }
}