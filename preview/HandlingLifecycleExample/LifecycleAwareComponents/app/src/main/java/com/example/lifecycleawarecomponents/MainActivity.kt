package com.example.lifecycleawarecomponents

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lifecycleawarecomponents.databinding.ActivityMainBinding

// AppCompatActivity는 ComponentActivity를 상속하고 있으므로 자체적인 lifecycle을 가지고 있다.
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var observer: MyObserver

    private val thread = Thread {
        while (isPlaying) binding.sbMusic.progress = mediaPlayer.currentPosition
    }

    private lateinit var mediaPlayer: MediaPlayer
    private var postistion = 0
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // add observer
        lifecycle.addObserver(observer)

        // music start
        binding.btnStart.setOnClickListener(musicStart())

        // music pause
        binding.btnPause.setOnClickListener(musicPause())

        // music restart
        binding.btnRestart.setOnClickListener(musicRestart())

        // music stop
        binding.btnStop.setOnClickListener(musicStop())
    }

    private fun musicStart(): (v: View) -> Unit = {
        isPlaying = true

        mediaPlayer = MediaPlayer.create(this, R.raw.chacha).apply {
            isLooping = false
            start()
        }

        binding.sbMusic.max = mediaPlayer.duration
        thread.start()

        binding.btnStart.visibility = View.INVISIBLE
        binding.btnPause.visibility = View.VISIBLE
        binding.btnStop.visibility = View.VISIBLE
    }


    private fun musicPause(): (v: View) -> Unit = {
        isPlaying = false

        postistion = mediaPlayer.currentPosition
        mediaPlayer.pause()

        binding.btnPause.visibility = View.INVISIBLE
        binding.btnStart.visibility = View.VISIBLE
    }

    private fun musicRestart(): (v: View) -> Unit = {
        isPlaying = true

        mediaPlayer.let {
            it.seekTo(postistion)
            it.start()
        }
        thread.start()

        binding.btnRestart.visibility = View.INVISIBLE
        binding.btnPause.visibility = View.VISIBLE
    }

    private fun musicStop(): (v: View) -> Unit = {
        isPlaying = false

        mediaPlayer.let {
            it.stop()
            it.release()
        }

        binding.btnStart.visibility = View.VISIBLE
        binding.btnPause.visibility = View.INVISIBLE
        binding.btnRestart.visibility = View.INVISIBLE
        binding.btnStop.visibility = View.INVISIBLE
        binding.sbMusic.progress = 0
    }

    /**
     *  lifecycleObserver 관리
     *  ex. mediaPlayer release
     */
    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}