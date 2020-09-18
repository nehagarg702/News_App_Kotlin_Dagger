package com.dell.example.newsApp.qualifiers

import javax.inject.Qualifier

@Qualifier
annotation class LoggingInterceptorQualifier {}

@Qualifier
annotation class ResponseCacheQualifier {}

@Qualifier
annotation class OfflineCacheQualifier {}

@Qualifier
annotation class ApplicationContext {}