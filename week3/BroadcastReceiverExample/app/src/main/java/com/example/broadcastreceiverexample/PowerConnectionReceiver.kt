package com.example.broadcastreceiverexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class PowerConnectionReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "PowerConnectionReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: ${intent?.action}")
        when (intent?.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                Toast.makeText(context, "충전기가 연결되었습니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onReceive: ACTION_POWER_CONNECTED")
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                Toast.makeText(context, "충전기가 해제되었습니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onReceive: ACTION_POWER_DISCONNECTED")
            }
        }
    }
}