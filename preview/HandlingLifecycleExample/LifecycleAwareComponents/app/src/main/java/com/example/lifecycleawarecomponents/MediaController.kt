package com.example.lifecycleawarecomponents

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import java.lang.ref.SoftReference

enum class State {
    PLAYING,
    PAUSED,
    STOPPED
}

object MediaController {
    private const val TAG = "MediaController"
    const val PROGRESS_KEY = "PROGRESS KEY"

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var state: State

    private lateinit var context: SoftReference<Context>
    private lateinit var seekBar: SoftReference<SeekBar>

    // with out ViewModel
    var liveProgress: MutableLiveData<Int>? = null

    fun connect(context: Context, seekBar: SeekBar): MediaController {
        state = State.STOPPED

        Log.d(TAG, "connect: $state")
        liveProgress = MutableLiveData(0)
        this.context = SoftReference(context)
        this.seekBar = SoftReference(seekBar)

        return this
    }

    /**
     *  @param progress start position
     */
    fun play(progress: Int = 0) {
        Log.d(TAG, "play: $state")

        if (progress > 0) liveProgress?.value = progress

        when (state) {
            State.STOPPED -> {
                state = State.PLAYING

                mediaPlayer = MediaPlayer.create(context.get(), R.raw.chacha)
                seekBar.get()?.max = mediaPlayer!!.duration - 50

                start()
            }
            State.PAUSED -> {
                state = State.PLAYING

                start()
            }
            else -> return
        }

    }

    private fun start() {
        mediaPlayer?.seekTo(liveProgress?.value ?: 0)
        mediaPlayer?.start()
        thread().start()
    }


    fun pause() {
        Log.d(TAG, "pause: $state")

        if (state == State.PLAYING) {
            state = State.PAUSED

            mediaPlayer?.pause()
        }
    }

    fun stop() {
        Log.d(TAG, "stop: $state")
        if (state != State.STOPPED) {
            state = State.STOPPED

            liveProgress?.value = 0
            mediaPlayer?.stop()
        }
    }

    private fun thread() = Thread {
        while (state == State.PLAYING) liveProgress?.postValue(mediaPlayer!!.currentPosition)
        if (state == State.STOPPED)
            liveProgress?.postValue(0)
    }

    fun disconnect() {
        Log.d(TAG, "disconnect: $state")

        stop()
        context.clear()
        seekBar.clear()
        liveProgress = null
        mediaPlayer?.release()
    }
}
