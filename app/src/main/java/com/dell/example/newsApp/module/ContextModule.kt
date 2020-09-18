package com.dell.example.newsApp.module

import android.content.Context
import com.dell.example.newsApp.qualifiers.ApplicationContext
import com.dell.example.newsApp.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(var context: Context) {

    @Provides
    @ApplicationContext
    @ApplicationScope
    fun context():Context
    {
        return context.applicationContext
    }
}