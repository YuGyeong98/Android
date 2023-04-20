package com.example.weather

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weather.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onStart() {
        super.onStart()
        checkAccessBackgroundLocationPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        }
    }

    private fun checkAccessBackgroundLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                ContextCompat.startForegroundService(
                    this,
                    Intent(this, UpdateWeatherService::class.java)
                )
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
                showPermissionRationaleDialog()
            }
            else -> {
                requestAccessBackgroundLocation()
            }
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("위치 권한을 허용해야 날씨를 표시할 수 있습니다.")
            setPositiveButton("확인") { _, _ ->
                requestAccessBackgroundLocation()
            }
            setNegativeButton("취소") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
        }.show()
    }

    private fun requestAccessBackgroundLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ),
                REQUEST_POST_NOTIFICATIONS
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                REQUEST_ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ACCESS_BACKGROUND_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.startForegroundService(
                        this,
                        Intent(this, UpdateWeatherService::class.java)
                    )
                } else {
                    AlertDialog.Builder(this).apply {
                        setMessage("위치 권한 거부로 인해 날씨를 표시할 수 없습니다. 설정 화면에서 직접 권한 허용을 해주세요.")
                        setPositiveButton("확인", null)
                    }.show()
                }
                return
            }
            REQUEST_POST_NOTIFICATIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.startForegroundService(
                        this,
                        Intent(this, UpdateWeatherService::class.java)
                    )
                } else {
                    AlertDialog.Builder(this).apply {
                        setMessage("Notification 권한 거부로 인해 서비스를 실행할 수 없습니다. 설정 화면에서 직접 권한 허용을 해주세요.")
                        setPositiveButton("확인", null)
                    }.show()
                }
                return
            }
        }
    }

    companion object {
        const val REQUEST_ACCESS_BACKGROUND_LOCATION = 100
        const val REQUEST_POST_NOTIFICATIONS = 200
    }
}