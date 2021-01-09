package com.ntetz.android.nyannyanengine_android.model.repository

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.config.DefaultTweetConfig
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.NoConnectivityException
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.IAccessToken
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterRequestMetadata
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignParam
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignature
import com.ntetz.android.nyannyanengine_android.util.Base64Encoder
import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface ITweetsRepository {
    suspend fun getLatestTweets(token: IAccessToken, scope: CoroutineScope, context: Context): List<Tweet>
    suspend fun getPreviousTweets(
        maxId: Long,
        token: IAccessToken,
        scope: CoroutineScope,
        context: Context
    ): List<Tweet>

    suspend fun postTweet(
        tweetBody: String,
        point: Int,
        token: IAccessToken,
        scope: CoroutineScope,
        context: Context
    ): Tweet
}

class TweetsRepository(
    private val twitterApi: ITwitterApi,
    private val twitterConfig: ITwitterConfig = TwitterConfig(),
    private val base64Encoder: IBase64Encoder = Base64Encoder()
) : ITweetsRepository {
    override suspend fun getLatestTweets(token: IAccessToken, scope: CoroutineScope, context: Context): List<Tweet> {
        val requestMetadata = TwitterRequestMetadata(
            method = TwitterEndpoints.homeTimelineMethod,
            path = TwitterEndpoints.homeTimelinePath,
            twitterConfig = twitterConfig
        )

        val authorization = TwitterSignature(
            requestMetadata = requestMetadata,
            twitterConfig = twitterConfig,
            base64Encoder = base64Encoder
        ).getOAuthValue(token)

        try {
            val result = getLatestTweetsFromWeb(authorization = authorization, scope = scope, context = context)
            val body = result?.body()?.toSortedById()
            if (result?.isSuccessful != true || body == null) {
                return getErrorTweets(result)
            }
            return body
        } catch (e: NoConnectivityException) {
            return DefaultTweetConfig.noConnectionRequestList
        } catch (e: Exception) {
            return DefaultTweetConfig.undefinedErrorList
        }
    }

    override suspend fun getPreviousTweets(
        maxId: Long,
        token: IAccessToken,
        scope: CoroutineScope,
        context: Context
    ): List<Tweet> {
        println("nya-n previous")
        val additionalHeaders = listOf(
            TwitterSignParam(
                TwitterEndpoints.homeTimelineCountParamName,
                TwitterEndpoints.homeTimelineCountParamDefaultValue
            ),
            TwitterSignParam(TwitterEndpoints.homeTimelineMaxIdParamName, maxId.toString())
        )

        val requestMetadata = TwitterRequestMetadata(
            additionalParams = additionalHeaders,
            method = TwitterEndpoints.homeTimelineMethod,
            appendAdditionalParamsToHead = true,
            path = TwitterEndpoints.homeTimelinePath,
            twitterConfig = twitterConfig
        )

        val authorization = TwitterSignature(
            requestMetadata = requestMetadata,
            twitterConfig = twitterConfig,
            base64Encoder = base64Encoder
        ).getOAuthValue(token)

        try {
            val result = getPreviousTweetsFromWeb(
                maxId = maxId.toString(),
                authorization = authorization,
                scope = scope,
                context = context
            )
            val body = result.body()?.toSortedById()
            if (!result.isSuccessful || body == null) {
                return getErrorTweets(result)
            }
            return body
        } catch (e: NoConnectivityException) {
            return DefaultTweetConfig.noConnectionRequestList
        } catch (e: Exception) {
            return DefaultTweetConfig.undefinedErrorList
        }
    }

    override suspend fun postTweet(
        tweetBody: String,
        point: Int,
        token: IAccessToken,
        scope: CoroutineScope,
        context: Context
    ): Tweet {
        val additionalHeaders = listOf(
            TwitterSignParam(
                TwitterEndpoints.postTweetStatusParamName,
                tweetBody
            )
        )

        val requestMetadata = TwitterRequestMetadata(
            additionalParams = additionalHeaders,
            method = TwitterEndpoints.postTweetMethod,
            path = TwitterEndpoints.postTweetPath,
            twitterConfig = twitterConfig
        )

        val authorization = TwitterSignature(
            requestMetadata = requestMetadata,
            twitterConfig = twitterConfig,
            base64Encoder = base64Encoder
        ).getOAuthValue(token)

        try {
            val result = postTweetToWeb(tweetBody, authorization, scope, context)
            val body = result.body()
            if (!result.isSuccessful || body == null) {
                return getErrorTweet(result)
            }
            return body.also { it.point = point }
        } catch (e: NoConnectivityException) {
            return DefaultTweetConfig.noConnectionRequestList[0]
        } catch (e: Exception) {
            return DefaultTweetConfig.undefinedErrorList[0]
        }
    }

    private suspend fun getLatestTweetsFromWeb(
        authorization: String,
        scope: CoroutineScope,
        context: Context
    ): Response<List<Tweet>>? {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterApi.getObjectClient(context)
                    .getTweets(
                        authorization = authorization
                    ).execute()
            }
        }
    }

    private suspend fun getPreviousTweetsFromWeb(
        maxId: String,
        authorization: String,
        scope: CoroutineScope,
        context: Context
    ): Response<List<Tweet>> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterApi.getObjectClient(context)
                    .getTweetsWithPage(
                        authorization = authorization,
                        count = TwitterEndpoints.homeTimelineCountParamDefaultValue,
                        maxId = maxId
                    )
                    .execute()
            }
        }
    }

    private suspend fun postTweetToWeb(
        tweetBody: String,
        authorization: String,
        scope: CoroutineScope,
        context: Context
    ): Response<Tweet> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterApi.getObjectClient(context)
                    .postTweet(
                        status = tweetBody,
                        authorization = authorization
                    )
                    .execute()
            }
        }
    }

    private fun getErrorTweets(result: Response<List<Tweet>>?): List<Tweet> {
        return when (result?.code()) {
            429 -> DefaultTweetConfig.tooManyRequestList
            else -> DefaultTweetConfig.undefinedErrorList
        }
    }

    private fun getErrorTweet(result: Response<Tweet>): Tweet {
        return when (result.code()) {
            429 -> DefaultTweetConfig.tooManyRequestList[0]
            else -> DefaultTweetConfig.undefinedErrorList[0]
        }
    }

    private fun List<Tweet>.toSortedById(): List<Tweet> {
        return this.toMutableList().apply { this.sortByDescending { it.id } }
    }
}
