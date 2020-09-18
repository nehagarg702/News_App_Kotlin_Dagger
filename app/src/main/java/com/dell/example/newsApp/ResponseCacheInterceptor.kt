package com.dell.example.newsApp

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.Response

/*****
 * Class to load the cached data in a minute
 */
@Module
class ResponseCacheInterceptor : Interceptor {

    @Provides
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + 60)
                .build()
    }
}