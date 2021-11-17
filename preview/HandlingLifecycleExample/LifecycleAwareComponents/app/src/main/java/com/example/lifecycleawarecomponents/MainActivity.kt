package com.example.lifecycleawarecomponents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.lifecycleawarecomponents.databinding.ActivityMainBinding

// AppCompatActivity는 ComponentActivity를 상속하고 있으므로 자체적인 lifecycle을 가지고 있다.
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var player: MediaController

    private lateinit var observer: MyObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // add observer
        // lifecycle.addObserver(observer)

        binding.sbMusic.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar?.max!! <= progress) {
                    player.stop()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                player.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                player.play()
            }
        })

        // music start
        binding.btnStart.setOnClickListener { player.play() }

        // music pause
        binding.btnPause.setOnClickListener { player.pause() }

        // music stop
        binding.btnStop.setOnClickListener { player.stop() }
    }

    /**
     *  lifecycleObserver 관리
     *  ex. mediaPlayer release
     */
    override fun onResume() {
        super.onResume()
        player = MediaController.player(this, binding.sbMusic)
    }

    override fun onPause() {
        super.onPause()
        player.release()
    }
}