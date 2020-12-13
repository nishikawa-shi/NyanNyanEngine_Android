package com.ntetz.android.nyannyanengine_android.model.dao.retrofit

import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ITwitterApiEndpoints {
    @POST(TwitterEndpoints.requestTokenPath)
    fun requestToken(@Header(TwitterEndpoints.authorizationHeaderName) authorization: String): Call<String>

    @POST(TwitterEndpoints.accessTokenPath)
    fun accessToken(
        @Query("oauth_verifier") oauthVerifier: String,
        @Query("oauth_token") oauthToken: String,
        @Header(TwitterEndpoints.authorizationHeaderName) authorization: String
    ): Call<String>
}
