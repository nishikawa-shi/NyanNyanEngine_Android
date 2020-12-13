package com.ntetz.android.nyannyanengine_android.model.repository

import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterRequestMetadata
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignature
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.util.Base64Encoder
import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface ITweetsRepository {
    suspend fun getTweets(user: TwitterUserRecord, scope: CoroutineScope): Response<String?>?
}

class TweetsRepository(
    private val twitterApi: ITwitterApi,
    private val twitterConfig: ITwitterConfig = TwitterConfig(),
    private val base64Encoder: IBase64Encoder = Base64Encoder()
) : ITweetsRepository {

    override suspend fun getTweets(user: TwitterUserRecord, scope: CoroutineScope): Response<String?>? {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
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

                twitterApi.client
                    .getTweets(
                        authorization = authorization
                    )
                    .execute()
            }
        }
    }
}
