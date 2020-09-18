package com.dell.example.newsApp.module

import android.content.Context
import com.dell.example.newsApp.MyTimesApplication
import com.dell.example.newsApp.OfflineResponseCacheInterceptor
import com.dell.example.newsApp.ResponseCacheInterceptor
import com.dell.example.newsApp.qualifiers.ApplicationContext
import com.dell.example.newsApp.qualifiers.LoggingInterceptorQualifier
import com.dell.example.newsApp.qualifiers.OfflineCacheQualifier
import com.dell.example.newsApp.qualifiers.ResponseCacheQualifier
import com.dell.example.newsApp.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [OfflineResponseCacheInterceptor::class,ResponseCacheInterceptor::class,ContextModule::class])
class OkHttpClientModule {

    @Provides
    @LoggingInterceptorQualifier
    fun getLoggingInterceptor(): Interceptor
    {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    fun getOkHttpClient(@LoggingInterceptorQualifier logging : Interceptor, @ResponseCacheQualifier response: Interceptor,
                        @OfflineCacheQualifier offlineResponse : Interceptor, cache:Cache): OkHttpClient
    {
        val httpClient = OkHttpClient.Builder()
        httpClient.addNetworkInterceptor(response)
        httpClient.addInterceptor(offlineResponse)
        httpClient.cache(cache)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)
        return httpClient.build()
    }

    @Provides
    fun getCache(file :File): Cache
    {
        return Cache(file, 10 * 1024 * 1024)
    }

    @Provides
    @ApplicationScope
    fun getFile(@ApplicationContext context:Context) :File{
        return File(context.cacheDir, "ResponsesCache")
    }

    @Provides
    @ResponseCacheQualifier
    fun getNetworkResponseInterceptor():Interceptor
    {
        return ResponseCacheInterceptor()
    }

    @Provides
    @OfflineCacheQualifier
    fun getOfflineCacheResponseInterceptor():Interceptor
    {
        return OfflineResponseCacheInterceptor()
    }
}