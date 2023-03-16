package com.example.recording

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.recording.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var recorder: MediaRecorder? = null
    private var fileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        binding.recordButton.setOnClickListener {
            record()
        }
    }

    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {

    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed $e")
            }
            start()
        }

        binding.recordButton.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.baseline_stop_circle_24
            )
        )
        binding.recordButton.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
        binding.playButton.isEnabled = false
        binding.playButton.alpha = 0.3f
        binding.stopButton.isEnabled = false
        binding.stopButton.alpha = 0.3f
    }

    private fun record() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                onRecord(true)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            ) -> {
                showPermissionRationaleDialog()
            }
            else -> {
                requestRecordAudio()
            }
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("녹음 권한을 허용해야 앱을 정상적으로 사용할 수 있습니다.")
            setPositiveButton("권한 허용하기") { _, _ ->
                requestRecordAudio()
            }
            setNegativeButton("취소", null)
        }.show()
    }

    private fun requestRecordAudio() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            REQUEST_RECORD_AUDIO_PERMISSION
        )
    }

    private fun showPermissionSettingDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("녹음 권한을 허용해야 앱을 정상적으로 사용할 수 있습니다. 앱 설정 화면에서 권한을 켜주세요.")
            setPositiveButton("권한 변경하기") { _, _ ->
                navigateToAppSetting()
            }
            setNegativeButton("취소", null)
        }.show()
    }

    private fun navigateToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onRecord(true)
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    showPermissionRationaleDialog()
                } else {
                    showPermissionSettingDialog()
                }
            }
        }
    }

    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        const val LOG_TAG = "AudioRecordTest"
    }
}