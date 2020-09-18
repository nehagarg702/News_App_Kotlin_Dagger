package com.dell.example.newsApp.network

import com.dell.example.newsApp.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
used to get the remaining url part used for getting the data
*/   interface ApiInterface {
    @GET("top-headlines")
    fun getHeadlines(@Query("sources") sources: String?,
                     @Query("apiKey") apiKey: String?): Call<NewsResponse?>?

    @GET("everything")
    fun getSearchResults(@Query("q") query: String?,
                         @Query("sortBy") sortBy: String?,
                         @Query("language") language: String?,
                         @Query("apiKey") apiKey: String?): Call<NewsResponse?>?
}