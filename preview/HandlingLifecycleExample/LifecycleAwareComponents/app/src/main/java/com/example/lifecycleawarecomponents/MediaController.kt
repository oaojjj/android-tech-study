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

    fun player(context: Context, seekBar: SeekBar): MediaController {
        state = State.STOPPED
        this.seekBar = SoftReference(seekBar)

        if (mediaPlayer == null) mediaPlayer = MediaPlayer.create(context, R.raw.chacha)
        return this
    }

    fun play() {
        when (state) {
            State.STOPPED -> {
                state = State.PLAYING

                mediaPlayer?.start()
                thread().start()
            }
            State.PAUSED -> {
                state = State.PLAYING

                mediaPlayer?.seekTo(prevPosition)
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
        state = State.STOPPED
        seekBar.get()?.progress = 0
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }

    private fun thread() = Thread {
        seekBar.get()?.max = mediaPlayer!!.duration
        while (state == State.PLAYING) seekBar.get()?.progress = mediaPlayer!!.currentPosition
    }


}
