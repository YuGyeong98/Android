package com.example.music_player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager

class MediaPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            PAUSE -> {
                mediaPlayer?.pause()
            }
            PLAY -> {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(applicationContext, R.raw.icefire)
                }
                mediaPlayer?.apply {
                    start()
                    setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
                }
            }
            STOP -> {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}