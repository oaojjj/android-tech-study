package com.example.lifecycleawarecomponents

import android.content.Context
import android.media.MediaPlayer
import android.widget.SeekBar
import java.lang.ref.SoftReference

enum class State {
    PLAYING,
    PAUSED,
    STOPPED
}

object MediaController {
    private var mediaPlayer: MediaPlayer? = null
    private var prevPosition = 0

    private lateinit var state: State

    private lateinit var seekBar: SoftReference<SeekBar>
    private lateinit var context: SoftReference<Context>

    fun player(context: Context, seekBar: SeekBar): MediaController {
        state = State.STOPPED

        this.context = SoftReference(context)
        this.seekBar = SoftReference(seekBar)
        return this
    }

    fun play() {
        when (state) {
            State.STOPPED -> {
                mediaPlayer = MediaPlayer.create(context.get(), R.raw.chacha)
                state = State.PLAYING

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
        if (state == State.PLAYING) {
            state = State.PAUSED

            prevPosition = mediaPlayer?.currentPosition ?: 0
            mediaPlayer?.pause()
        }
    }

    fun stop() {
        if (state != State.STOPPED) {
            state = State.STOPPED
            mediaPlayer?.stop()
        }
    }

    private fun thread() = Thread {
        seekBar.get()?.max = mediaPlayer!!.duration
        while (state == State.PLAYING) seekBar.get()?.progress = mediaPlayer!!.currentPosition
        if (state == State.STOPPED)
            seekBar.get()?.progress = 0
    }

    fun release() {
        stop()
        context.clear()
        seekBar.clear()
        mediaPlayer?.release()
    }

}
