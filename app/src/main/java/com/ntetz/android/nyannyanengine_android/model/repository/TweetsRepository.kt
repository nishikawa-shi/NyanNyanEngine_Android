package com.ntetz.android.nyannyanengine_android.model.repository

import com.ntetz.android.nyannyanengine_android.model.config.DefaultTweetConfig
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.room.ICachedTweetsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterRequestMetadata
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignParam
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignature
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.CachedTweetRecord
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.util.Base64Encoder
import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ITweetsRepository {
    suspend fun getTweets(user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>?
    suspend fun getLatestTweets(user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>?
    suspend fun getPreviousTweets(maxId: Long, user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>?
}

class TweetsRepository(
    private val twitterApi: ITwitterApi,
    private val twitterConfig: ITwitterConfig = TwitterConfig(),
    private val base64Encoder: IBase64Encoder = Base64Encoder(),
    private val cachedTweetsDao: ICachedTweetsDao
) : ITweetsRepository {

    override suspend fun getTweets(user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>? {
        val cache = getTweetsFromCache(scope)
        if (cache.isNotEmpty()) {
            return cache
        }
        return fetchLatestTweets(user, scope)
    }

    override suspend fun getLatestTweets(user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>? {
        return fetchLatestTweets(user, scope)
    }

    override suspend fun getPreviousTweets(maxId: Long, user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>? {
        return getPreviousTweets(maxId = maxId.toString(), user = user, scope = scope)
    }

    private suspend fun getTweetsFromCache(scope: CoroutineScope): List<Tweet> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                cachedTweetsDao.getAll().toTweets()
            }
        }
    }

    private suspend fun fetchLatestTweets(user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>? {
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

        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                val result = twitterApi.objectClient
                    .getTweets(
                        authorization = authorization
                    )
                    .execute()

                if (!result.isSuccessful) {
                    when (result.code()) {
                        429 -> DefaultTweetConfig.tooManyRequestList
                        else -> DefaultTweetConfig.undefinedErrorList
                    }
                }
                result.body().also {
                    cachedTweetsDao.deleteAll()
                    cachedTweetsDao.upsert(it?.toCachedTweetRecords() ?: listOf())
                }
            }
        }
    }

    private suspend fun getPreviousTweets(maxId: String, user: TwitterUserRecord, scope: CoroutineScope): List<Tweet>? {
        val additionalHeaders = listOf(
            TwitterSignParam(
                TwitterEndpoints.homeTimelineCountParamName,
                TwitterEndpoints.homeTimelineCountParamDefaultValue
            ),
            TwitterSignParam(TwitterEndpoints.homeTimelineMaxIdParamName, maxId)
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

        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                val result = twitterApi.objectClient
                    .getTweetsWithPage(
                        authorization = authorization,
                        count = TwitterEndpoints.homeTimelineCountParamDefaultValue,
                        maxId = maxId
                    )
                    .execute()

                if (!result.isSuccessful) {
                    when (result.code()) {
                        429 -> DefaultTweetConfig.tooManyRequestList
                        else -> DefaultTweetConfig.undefinedErrorList
                    }
                }
                result.body()
            }
        }
    }

    private fun List<Tweet>.toCachedTweetRecords(): List<CachedTweetRecord> {
        return this.map {
            CachedTweetRecord(
                id = it.id,
                text = it.text,
                createdAt = it.createdAt,
                userName = it.user.name,
                userScreenName = it.user.screenName,
                profileImageUrlHttps = it.user.profileImageUrlHttps ?: ""
            )
        }
    }

    private fun List<CachedTweetRecord>.toTweets(): List<Tweet> {
        return this.map {
            Tweet(
                id = it.id,
                text = it.text,
                createdAt = it.createdAt,
                user = User(
                    name = it.userName,
                    screenName = it.userScreenName,
                    profileImageUrlHttps = it.profileImageUrlHttps
                )
            )
        }
    }
}
