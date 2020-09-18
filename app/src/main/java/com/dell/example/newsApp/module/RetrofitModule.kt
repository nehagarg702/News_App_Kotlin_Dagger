package com.dell.example.newsApp.module

import com.dell.example.newsApp.model.Constants
import com.dell.example.newsApp.network.ApiInterface
import com.dell.example.newsApp.scope.ApplicationScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [OkHttpClientModule::class])
class RetrofitModule {

    @Provides
    fun getRetrofitApi(retrofit: Retrofit):ApiInterface
    {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @ApplicationScope
    fun getRetrofit(okHttpClient: OkHttpClient):Retrofit
    {
        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    fun getGsonConvertorFactory(gson:Gson) : GsonConverterFactory
    {
        return GsonConverterFactory.create(gson)
    }
}