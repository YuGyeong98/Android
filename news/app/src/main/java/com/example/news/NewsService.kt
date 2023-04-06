package com.example.news

import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
    @GET("news/SectionRssFeed.do?sectionId=01&plink=RSSREADER")
    fun politicsNews(): Call<News>

    @GET("news/SectionRssFeed.do?sectionId=02&plink=RSSREADER")
    fun economyNews(): Call<News>

    @GET("news/SectionRssFeed.do?sectionId=03&plink=RSSREADER")
    fun societyNews(): Call<News>

    @GET("news/SectionRssFeed.do?sectionId=07&plink=RSSREADER")
    fun lifeNews(): Call<News>

    @GET("news/SectionRssFeed.do?sectionId=08&plink=RSSREADER")
    fun globalNews(): Call<News>

    @GET("news/SectionRssFeed.do?sectionId=14&plink=RSSREADER")
    fun entertainmentNews(): Call<News>

    @GET("news/SectionRssFeed.do?sectionId=09&plink=RSSREADER")
    fun sportsNews(): Call<News>
}