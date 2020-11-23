package com.ntetz.android.nyannyanengine_android.model.repository

import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterRequestMetadata
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignParam
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignature
import com.ntetz.android.nyannyanengine_android.util.Base64Encoder
import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface IAccountRepository {
    suspend fun getAuthorizationToken(scope: CoroutineScope): String?
}

class AccountRepository(
    private val twitterApi: ITwitterApi,
    private val twitterConfig: ITwitterConfig = TwitterConfig(),
    private val base64Encoder: IBase64Encoder = Base64Encoder()
) : IAccountRepository {
    override suspend fun getAuthorizationToken(scope: CoroutineScope): String? {
        val additionalHeaders = listOf(
            TwitterSignParam(TwitterEndpoints.requestTokenOauthCallbackParamName, twitterConfig.callbackUrl)
        )
        return withContext(scope.coroutineContext) {
            val requestMetadata = TwitterRequestMetadata(
                additionalParams = additionalHeaders,
                method = TwitterEndpoints.requestTokenMethod,
                path = TwitterEndpoints.requestTokenPath,
                twitterConfig = twitterConfig
            )
            val authorization = TwitterSignature(
                requestMetadata = requestMetadata,
                twitterConfig = twitterConfig,
                base64Encoder = base64Encoder
            ).getOAuthValue()

            withContext(Dispatchers.IO) {
                twitterApi.client
                    .requestToken(authorization)
                    .execute()
                    .body()
            }
        }
    }
}