package com.example.good_place_map

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SearchRepository {
    private val client = OkHttpClient.Builder().addInterceptor(AppInterceptor()).build()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
    private val service = retrofit.create(SearchService::class.java)

    fun getGoodPlace(query: String): Call<SearchResult> {
        return service.getGoodPlace("$query 맛집", 5)
    }

    class AppInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("X-Naver-Client-Id", MyApplication.applicationContext.getString(R.string.naver_search_client_id))
                .addHeader("X-Naver-Client-Secret", MyApplication.applicationContext.getString(R.string.naver_search_client_secret))
                .build()
            return chain.proceed(request)
        }
    }
}