package com.example.lifecycleawarecomponents

import android.content.Context
import android.util.Log
import androidx.lifecycle.*

class MyObserver(
    private val context: Context,
    private val lifeCycle: Lifecycle,
    callback: () -> Unit
) :
    DefaultLifecycleObserver {

    companion object {
        const val TAG = "MyObserver"
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d(TAG, "onCreate")
        // Log.d(TAG, Log.getStackTraceString(Exception()))
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d(TAG, "onStart")
        if (lifeCycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            Log.d(TAG, "currentState is greater or equal to INITIALIZED")
        }
        if (lifeCycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            Log.d(TAG, "currentState is greater or equal to CREATED")
        }
        if (lifeCycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            Log.d(TAG, "currentState is greater or equal to STARTED")
        }
        if (lifeCycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            Log.d(TAG, "currentState is greater or equal to RESUMED")
        }
        if (lifeCycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)) {
            Log.d(TAG, "currentState is greater or equal to DESTROYED")
        }

    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d(TAG, "onResume")

    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d(TAG, "onPause")

    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d(TAG, "onStop")

    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d(TAG, "onDestroy")

    }

}