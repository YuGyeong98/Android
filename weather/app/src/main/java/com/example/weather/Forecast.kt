package com.example.weather

data class Forecast(
    val forecastDate: String,
    val forecastTime: String,
    var pop: Int = 0,
    var pty: String = "",
    var sky: String = "",
    var tmp: Double = 0.0,
) {
    val weather: String
        get() {
            return if (pty == "" || pty == "없음") sky else pty
        }
}