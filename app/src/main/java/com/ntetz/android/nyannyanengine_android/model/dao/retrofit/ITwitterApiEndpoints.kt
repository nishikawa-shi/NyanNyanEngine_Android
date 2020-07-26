package com.ntetz.android.nyannyanengine_android.model.dao.retrofit

import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface ITwitterApiEndpoints {
    @POST(TwitterEndpoints.requestTokenPath)
    fun requestToken(@Header(TwitterEndpoints.authorizationHeaderName) authorization: String): Call<String>
}
