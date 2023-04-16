package com.example.weather

import java.time.LocalDateTime
import java.time.LocalTime

data class BaseDateTime(
    val baseDate: String,
    val baseTime: String,
) {
    companion object {
        fun getBaseDateTime(): BaseDateTime {
            var localDateTime = LocalDateTime.now()
            val baseDate = String.format(
                "%04d%02d%02d",
                localDateTime.year,
                localDateTime.monthValue,
                localDateTime.dayOfMonth
            )
            val baseTime = when (localDateTime.toLocalTime()) {
                in LocalTime.of(0, 0)..LocalTime.of(2, 30) -> {
                    localDateTime = localDateTime.minusDays(1)
                    "2300"
                }
                in LocalTime.of(2, 30)..LocalTime.of(5, 30) -> {
                    "0200"
                }
                in LocalTime.of(5, 30)..LocalTime.of(8, 30) -> {
                    "0500"
                }
                in LocalTime.of(8, 30)..LocalTime.of(11, 30) -> {
                    "0800"
                }
                in LocalTime.of(11, 30)..LocalTime.of(14, 30) -> {
                    "1100"
                }
                in LocalTime.of(14, 30)..LocalTime.of(17, 30) -> {
                    "1400"
                }
                in LocalTime.of(17, 30)..LocalTime.of(20, 30) -> {
                    "1700"
                }
                in LocalTime.of(20, 30)..LocalTime.of(23, 30) -> {
                    "2000"
                }
                else -> {
                    "2300"
                }
            }
            return BaseDateTime(baseDate, baseTime)
        }
    }
}