package com.example.recording

import android.os.Handler
import android.os.Looper

class Timer(listener: OnTimerTickListener) {
    private var duration = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            duration += 40L
            listener.onTick(duration)
            handler.postDelayed(this, 40L)
        }
    }

    fun start() {
        handler.post(runnable)
    }

    fun stop() {
        handler.removeCallbacks(runnable)
        duration = 0L
    }
}

interface OnTimerTickListener {
    fun onTick(duration: Long)
}