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
    val scalarClient: ITwitterApiEndpoints
    val objectClient: ITwitterApiEndpoints
}

class TwitterApi(private val context: Context) : ITwitterApi {
    override val scalarClient = createScalarClient()
    override val objectClient = createObjectClient()

    private fun createScalarClient(): ITwitterApiEndpoints {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(NetworkConnectionInterceptor(context)).build()
        return Retrofit
            .Builder()
            .baseUrl(TwitterEndpoints.baseEndpoint)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    private fun createObjectClient(): ITwitterApiEndpoints {
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
