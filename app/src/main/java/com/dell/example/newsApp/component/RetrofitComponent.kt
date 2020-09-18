package com.dell.example.newsApp.component

import com.dell.example.newsApp.module.RetrofitModule
import com.dell.example.newsApp.network.ApiInterface
import com.dell.example.newsApp.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [RetrofitModule::class])
interface RetrofitComponent {
    fun getRetrofitUri() : ApiInterface
}