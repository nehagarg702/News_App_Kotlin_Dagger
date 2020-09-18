package com.dell.example.newsApp

import android.content.Context
import android.net.ConnectivityManager
import com.dell.example.newsApp.MyTimesApplication.Companion.myTimesApplicationInstance

/****
 * Class to check the network connection
 */
object UtilityMethods {
    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = myTimesApplicationInstance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting)
        }
}