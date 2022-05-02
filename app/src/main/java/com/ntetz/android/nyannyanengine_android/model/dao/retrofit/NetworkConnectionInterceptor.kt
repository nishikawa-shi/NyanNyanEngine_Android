package com.ntetz.android.nyannyanengine_android.model.dao.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NoConnectivityException()
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private val isConnected: Boolean
        get() {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
            val primaryNetwork = connectivityManager.allNetworks.firstOrNull() ?: return false
            val netInfo = connectivityManager.getNetworkCapabilities(primaryNetwork) ?: return false
            return netInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
}
