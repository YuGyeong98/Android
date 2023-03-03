package com.example.music_player

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.music_player.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()
    }

    private fun pause() {
        Intent(this, MediaPlayerService::class.java).also { intent ->
            intent.action = PAUSE
            startService(intent)
        }
    }

    private fun play() {
        Intent(this, MediaPlayerService::class.java).also { intent ->
            intent.action = PLAY
            startService(intent)
        }
    }

    private fun stop() {
        Intent(this, MediaPlayerService::class.java).also { intent ->
            intent.action = STOP
            startService(intent)
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                binding.pauseButton.setOnClickListener { pause() }
                binding.playButton.setOnClickListener { play() }
                binding.stopButton.setOnClickListener { stop() }
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestPostNotification()
            }
        }
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("알림을 표시하기 위해 노티피케이션 권한이 필요합니다.")
            setPositiveButton("확인") { _, _ ->
                requestPostNotification()
            }
            setNegativeButton("취소", null)
        }.show()
    }

    private fun requestPostNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_ID
            )
        }
    }
}