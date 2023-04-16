package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WeatherRepository.getVillageForecast(
            serviceKey = getString(R.string.weather_service_key),
            latitude = 37.3592,
            longitude = 127.1048,
            successCallback = { forecastList ->
                val currentForecast = forecastList.first()
                binding.temperatureTextView.text = getString(R.string.temperature_text, currentForecast.tmp)
                binding.skyTextView.text = currentForecast.sky
                binding.precipitationTextView.text = getString(R.string.precipitation_text, currentForecast.pop)
            },
            failureCallback = { throwable ->
                throwable.printStackTrace()
            }
        )
    }
}