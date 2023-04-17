package com.example.weather

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.databinding.ItemForecastBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAccessCoarseLocationPermission()
    }

    private fun getVillageForecast() {
        WeatherRepository.getVillageForecast(
            serviceKey = getString(R.string.weather_service_key),
            latitude = 37.3592,
            longitude = 127.1048,
            successCallback = { forecastList ->
                val currentForecast = forecastList.first()
                binding.temperatureTextView.text = getString(R.string.temperature_text, currentForecast.tmp)
                binding.skyTextView.text = currentForecast.sky
                binding.precipitationTextView.text = getString(R.string.precipitation_text, currentForecast.pop)
                binding.forecastLinearLayout.apply {
                    forecastList.forEachIndexed { index, forecast ->
                        if (index == 0) {
                            return@forEachIndexed
                        }
                        val itemView = ItemForecastBinding.inflate(layoutInflater)
                        itemView.timeTextView.text = forecast.forecastTime.take(2).plus("시")
                        itemView.weatherTextView.text = forecast.weather
                        itemView.temperatureTextView.text = getString(R.string.temperature_text, forecast.tmp)
                        addView(itemView.root)
                    }
                }
            },
            failureCallback = { throwable ->
                throwable.printStackTrace()
            }
        )
    }

    private fun checkAccessCoarseLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getVillageForecast()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                showPermissionRationaleDialog()
            }
            else -> {
                requestAccessCoarseLocation()
            }
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("위치 권한을 허용해야 날씨를 표시할 수 있습니다.")
            setPositiveButton("확인") { _, _ ->
                requestAccessCoarseLocation()
            }
            setNegativeButton("취소") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
        }.show()
    }

    private fun requestAccessCoarseLocation() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_ACCESS_COARSE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ACCESS_COARSE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getVillageForecast()
                } else {
                    AlertDialog.Builder(this).apply {
                        setMessage("위치 권한 거부로 인해 날씨를 표시할 수 없습니다. 설정 화면에서 직접 권한 허용을 해주세요.")
                        setPositiveButton("확인", null)
                    }.show()
                }
                return
            }
        }
    }

    companion object {
        const val REQUEST_ACCESS_COARSE_LOCATION = 100
    }
}