package com.ntetz.android.nyannyanengine_android.model.dao.retrofit

import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

interface ITwitterApi {
    val client: ITwitterApiEndpoints
}

object TwitterApi : ITwitterApi {
    override val client: ITwitterApiEndpoints = Retrofit
        .Builder()
        .baseUrl(TwitterEndpoints.baseEndpoint)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create()
}
