package com.dell.example.newsApp

import android.app.Application

class MyTimesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        myTimesApplicationInstance = this
    }

    companion object {
        @JvmStatic
        lateinit var myTimesApplicationInstance: MyTimesApplication
    }
}