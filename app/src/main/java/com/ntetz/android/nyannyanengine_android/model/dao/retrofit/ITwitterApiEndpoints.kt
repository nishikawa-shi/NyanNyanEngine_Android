package com.ntetz.android.nyannyanengine_android.model.dao.retrofit

import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessTokenInvalidation
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ITwitterApiEndpoints {
    @POST(TwitterEndpoints.requestTokenPath)
    fun requestToken(@Header(TwitterEndpoints.authorizationHeaderName) authorization: String): Call<String>

    @POST(TwitterEndpoints.accessTokenPath)
    fun accessToken(
        @Query(TwitterEndpoints.accessTokenOauthVerifierParamName) oauthVerifier: String,
        @Query(TwitterEndpoints.accessTokenOauthTokenParamName) oauthToken: String,
        @Header(TwitterEndpoints.authorizationHeaderName) authorization: String
    ): Call<String>

    @GET(TwitterEndpoints.verifyCredentialsPath)
    fun verifyCredentials(
        @Header(TwitterEndpoints.authorizationHeaderName) authorization: String
    ): Call<User>

    @POST(TwitterEndpoints.invalidateTokenPath)
    fun invalidateToken(
        @Header(TwitterEndpoints.authorizationHeaderName) authorization: String
    ): Call<AccessTokenInvalidation>

    @GET(TwitterEndpoints.homeTimelinePath)
    fun getTweets(@Header(TwitterEndpoints.authorizationHeaderName) authorization: String): Call<List<Tweet>>

    @GET(TwitterEndpoints.homeTimelinePath)
    fun getTweetsWithPage(
        @Query(TwitterEndpoints.homeTimelineMaxIdParamName) maxId: String,
        @Query(TwitterEndpoints.homeTimelineCountParamName) count: String,
        @Header(TwitterEndpoints.authorizationHeaderName) authorization: String
    ): Call<List<Tweet>>

    @POST(TwitterEndpoints.postTweetPath)
    fun postTweet(
        @Query(TwitterEndpoints.postTweetStatusParamName) status: String,
        @Header(TwitterEndpoints.authorizationHeaderName) authorization: String
    ): Call<Tweet>
}
