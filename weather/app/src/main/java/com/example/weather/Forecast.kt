package com.example.weather

data class Forecast(
    val forecastDate: String,
    val forecastTime: String,
    var pop: Int = 0,
    var pty: String = "",
    var sky: String = "",
    var tmp: Double = 0.0,
)