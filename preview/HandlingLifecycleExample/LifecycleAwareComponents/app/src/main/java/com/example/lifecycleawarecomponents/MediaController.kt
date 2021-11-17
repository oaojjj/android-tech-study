package com.example.lifecycleawarecomponents

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.SeekBar
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
    private var prevPosition = 0

    private lateinit var state: State

    private lateinit var context: SoftReference<Context>
    private lateinit var seekBar: SoftReference<SeekBar>

    fun connect(context: Context, seekBar: SeekBar): MediaController {
        state = State.STOPPED

        Log.d(TAG, "connect: $state")
        this.context = SoftReference(context)
        this.seekBar = SoftReference(seekBar)

        return this
    }

    fun play(progress: Int = 0) {
        Log.d(TAG, "play: $state")
        when (state) {
            State.STOPPED -> {
                state = State.PLAYING

                mediaPlayer = MediaPlayer.create(context.get(), R.raw.chacha)
                seekBar.get()?.max = mediaPlayer!!.duration


                mediaPlayer?.seekTo(progress)
                mediaPlayer?.start()
                thread().start()
            }
            State.PAUSED -> {
                state = State.PLAYING

                val currentProgress = seekBar.get()!!.progress

                if (prevPosition == currentProgress) mediaPlayer?.seekTo(prevPosition)
                else mediaPlayer?.seekTo(currentProgress)

                mediaPlayer?.start()
                thread().start()
            }
            else -> return
        }
    }

    fun pause() {
        Log.d(TAG, "pause: $state")
        if (state == State.PLAYING) {
            state = State.PAUSED

            prevPosition = mediaPlayer?.currentPosition ?: 0
            mediaPlayer?.pause()
        }
    }

    fun stop() {
        Log.d(TAG, "stop: $state")
        if (state != State.STOPPED) {
            state = State.STOPPED

            mediaPlayer?.stop()
        }
    }

    private fun thread() = Thread {
        while (state == State.PLAYING) seekBar.get()?.progress = mediaPlayer!!.currentPosition
        if (state == State.STOPPED)
            seekBar.get()?.progress = 0
    }

    fun disconnect() {
        Log.d(TAG, "disconnect: $state")
        stop()
        context.clear()
        seekBar.clear()
        mediaPlayer?.release()
    }

}
