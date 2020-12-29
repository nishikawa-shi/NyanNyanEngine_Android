package com.ntetz.android.nyannyanengine_android.model.repository

import com.ntetz.android.nyannyanengine_android.model.config.DefaultTweetConfig
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterRequestMetadata
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignParam
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignature
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.util.Base64Encoder
import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface ITweetsRepository {
    suspend fun getLatestTweets(user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>
    suspend fun getPreviousTweets(maxId: Long, user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>
}

class TweetsRepository(
    private val twitterApi: ITwitterApi,
    private val twitterConfig: ITwitterConfig = TwitterConfig(),
    private val base64Encoder: IBase64Encoder = Base64Encoder()
) : ITweetsRepository {
    override suspend fun getLatestTweets(user: TwitterUserRecord, scope: CoroutineScope): List<Tweet> {
        val requestMetadata = TwitterRequestMetadata(
            method = TwitterEndpoints.homeTimelineMethod,
            path = TwitterEndpoints.homeTimelinePath,
            twitterConfig = twitterConfig
        )

        val authorization = TwitterSignature(
            requestMetadata = requestMetadata,
            twitterConfig = twitterConfig,
            base64Encoder = base64Encoder
        ).getOAuthValue(user)

        val result = getLatestTweetsFromWeb(authorization = authorization, scope = scope)
        val body = result.body()?.toSortedById()
        if (!result.isSuccessful || body == null) {
            return getErrorTweets(result)
        }
        return body
    }

    override suspend fun getPreviousTweets(maxId: Long, user: TwitterUserRecord, scope: CoroutineScope): List<Tweet> {
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
            path = TwitterEndpoints.homeTimelinePath,
            twitterConfig = twitterConfig
        )

        val authorization = TwitterSignature(
            requestMetadata = requestMetadata,
            twitterConfig = twitterConfig,
            base64Encoder = base64Encoder
        ).getOAuthValue(user)

        val result = getPreviousTweetsFromWeb(maxId = maxId.toString(), authorization = authorization, scope = scope)
        val body = result.body()?.toSortedById()
        if (!result.isSuccessful || body == null) {
            return getErrorTweets(result)
        }
        return body
    }

    private suspend fun getLatestTweetsFromWeb(
        authorization: String,
        scope: CoroutineScope
    ): Response<List<Tweet>> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterApi.objectClient
                    .getTweets(
                        authorization = authorization
                    )
                    .execute()
            }
        }
    }

    private suspend fun getPreviousTweetsFromWeb(
        maxId: String,
        authorization: String,
        scope: CoroutineScope
    ): Response<List<Tweet>> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterApi.objectClient
                    .getTweetsWithPage(
                        authorization = authorization,
                        count = TwitterEndpoints.homeTimelineCountParamDefaultValue,
                        maxId = maxId
                    )
                    .execute()
            }
        }
    }

    private fun getErrorTweets(result: Response<List<Tweet>>): List<Tweet> {
        return when (result.code()) {
            429 -> DefaultTweetConfig.tooManyRequestList
            else -> DefaultTweetConfig.undefinedErrorList
        }
    }

    private fun List<Tweet>.toSortedById(): List<Tweet> {
        return this.toMutableList().apply { this.sortByDescending { it.id } }
    }
}
