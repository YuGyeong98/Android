package com.example.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://apis.data.go.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(WeatherService::class.java)
}