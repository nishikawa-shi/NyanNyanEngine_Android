package com.ntetz.android.nyannyanengine_android.model.dao.retrofit

import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

interface ITwitterApi {
    val client: ITwitterApiEndpoints
    val objectClient: ITwitterApiEndpoints
}

object TwitterApi : ITwitterApi {
    override val client: ITwitterApiEndpoints = Retrofit
        .Builder()
        .baseUrl(TwitterEndpoints.baseEndpoint)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create()

    override val objectClient: ITwitterApiEndpoints
        get() {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            return Retrofit
                .Builder()
                .baseUrl(TwitterEndpoints.baseEndpoint)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create()
        }
}
