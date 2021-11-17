package com.example.lifecycleawarecomponents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.lifecycleawarecomponents.databinding.ActivityMainBinding

// AppCompatActivity는 ComponentActivity를 상속하고 있으므로 자체적인 lifecycle을 가지고 있다.
class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var player: MediaController

    private lateinit var observer: MyObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")

        player = MediaController.connect(this, binding.sbMusic).apply {
            this.liveProgress?.observe(this@MainActivity) {
                // UI Update
                binding.sbMusic.progress = it
            }
        }

        // 이렇게 사용하는게 맞나 모르겠음..
        observer = MyObserver(player)

        // add observer
        lifecycle.addObserver(observer)

        binding.sbMusic.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Log.d(TAG, "onProgressChanged: ${seekBar?.max},$progress")
                // 왜 drag 하고나면 max랑 progress랑 max값이 10~30 정도 오차가 생김.. why?
                if (seekBar?.max!! <= progress) player.stop()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d(TAG, "onStartTrackingTouch: ${seekBar?.progress}")
                player.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                MediaController.restart(seekBar?.progress)
            }
        })

        // music start
        binding.btnStart.setOnClickListener { player.play() }

        // music pause
        binding.btnPause.setOnClickListener { player.pause() }

        // music stop
        binding.btnStop.setOnClickListener { player.stop() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(MediaController.PROGRESS_KEY, binding.sbMusic.progress)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        MediaController.restart(savedInstanceState.getInt(MediaController.PROGRESS_KEY))
        super.onRestoreInstanceState(savedInstanceState)
    }
}