package com.example.weather

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://apis.data.go.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(WeatherService::class.java)

    fun getVillageForecast(
        serviceKey: String,
        latitude: Double,
        longitude: Double,
        successCallback: (List<Forecast>) -> Unit,
        failureCallback: (Throwable) -> Unit,
    ) {
        val baseDateTime = BaseDateTime.getBaseDateTime()
        val geoPointConverter = GeoPointConverter()
        val point = geoPointConverter.convert(latitude, longitude)

        service.getVillageForecast(
            serviceKey = serviceKey,
            baseDate = baseDateTime.baseDate,
            baseTime = baseDateTime.baseTime,
            nx = point.nx,
            ny = point.ny,
        ).enqueue(object : Callback<WeatherEntity> {
            override fun onResponse(call: Call<WeatherEntity>, response: Response<WeatherEntity>) {
                val forecastList =
                    response.body()?.response?.body?.items?.forecastEntityList.orEmpty()
                val forecastMap = mutableMapOf<String, Forecast>()
                for (forecast in forecastList) {
                    val key = "${forecast.forecastDate}/${forecast.forecastTime}"
                    if (forecastMap[key] == null) {
                        forecastMap[key] = Forecast(forecast.forecastDate, forecast.forecastTime)
                    }
                    forecastMap[key]?.apply {
                        when (forecast.category) {
                            Category.POP -> {
                                pop = forecast.forecastValue.toInt()
                            }
                            Category.PTY -> {
                                pty = transformPty(forecast)
                            }
                            Category.SKY -> {
                                sky = transformSky(forecast)
                            }
                            Category.TMP -> {
                                tmp = forecast.forecastValue.toDouble()
                            }
                            else -> {}
                        }
                    }
                }

                val forecastDataList = forecastMap.values.toMutableList()
                if (forecastDataList.isEmpty()) {
                    failureCallback(NullPointerException())
                } else {
                    successCallback(forecastDataList)
                }
            }

            override fun onFailure(call: Call<WeatherEntity>, t: Throwable) {
                failureCallback(t)
            }
        })
    }

    private fun transformPty(forecast: ForecastEntity): String {
        return when (forecast.forecastValue.toInt()) {
            0 -> "없음"
            1 -> "비"
            2 -> "비/눈"
            3 -> "눈"
            4 -> "소나기"
            else -> ""
        }
    }

    private fun transformSky(forecast: ForecastEntity): String {
        return when (forecast.forecastValue.toInt()) {
            1 -> "맑음"
            3 -> "구름많음"
            4 -> "흐림"
            else -> ""
        }
    }
}