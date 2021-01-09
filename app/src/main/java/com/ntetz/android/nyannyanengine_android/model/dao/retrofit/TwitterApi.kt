package com.ntetz.android.nyannyanengine_android.model.dao.retrofit

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

interface ITwitterApi {
    fun getScalarClient(context: Context): ITwitterApiEndpoints
    fun getObjectClient(context: Context): ITwitterApiEndpoints
}

object TwitterApi : ITwitterApi {
    override fun getScalarClient(context: Context): ITwitterApiEndpoints {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(NetworkConnectionInterceptor(context)).build()
        return Retrofit
            .Builder()
            .baseUrl(TwitterEndpoints.baseEndpoint)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    override fun getObjectClient(context: Context): ITwitterApiEndpoints {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(NetworkConnectionInterceptor(context)).build()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit
            .Builder()
            .baseUrl(TwitterEndpoints.baseEndpoint)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create()
    }
}
