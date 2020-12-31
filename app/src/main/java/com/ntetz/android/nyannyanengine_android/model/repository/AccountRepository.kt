package com.ntetz.android.nyannyanengine_android.model.repository

import android.net.Uri
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.room.ITwitterUserDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessToken
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessTokenInvalidation
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

interface IAccountRepository {
    suspend fun getAuthorizationToken(scope: CoroutineScope): String?
    suspend fun getAccessToken(oauthVerifier: String, oauthToken: String, scope: CoroutineScope): AccessToken
    suspend fun loadTwitterUser(scope: CoroutineScope): TwitterUserRecord?
    suspend fun saveTwitterUser(record: TwitterUserRecord, scope: CoroutineScope)
    suspend fun deleteTwitterUser(user: TwitterUserRecord, scope: CoroutineScope): AccessTokenInvalidation?
}

class AccountRepository(
    private val twitterApi: ITwitterApi,
    private val twitterConfig: ITwitterConfig = TwitterConfig(),
    private val base64Encoder: IBase64Encoder = Base64Encoder(),
    private val twitterUserDao: ITwitterUserDao
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
                appendAdditionalParamsToHead = true,
                twitterConfig = twitterConfig
            )
            val authorization = TwitterSignature(
                requestMetadata = requestMetadata,
                twitterConfig = twitterConfig,
                base64Encoder = base64Encoder
            ).getOAuthValue()

            withContext(Dispatchers.IO) {
                twitterApi.scalarClient
                    .requestToken(authorization)
                    .execute()
                    .body()
            }
        }
    }

    override suspend fun getAccessToken(
        oauthVerifier: String,
        oauthToken: String,
        scope: CoroutineScope
    ): AccessToken {
        return withContext(scope.coroutineContext) {
            val requestMetadata = TwitterRequestMetadata(
                method = TwitterEndpoints.accessTokenMethod,
                path = TwitterEndpoints.accessTokenPath,
                twitterConfig = twitterConfig
            )
            val authorization = TwitterSignature(
                requestMetadata = requestMetadata,
                twitterConfig = twitterConfig,
                base64Encoder = base64Encoder
            ).getOAuthValue()

            withContext(Dispatchers.IO) {
                twitterApi.scalarClient
                    .accessToken(
                        oauthVerifier = oauthVerifier,
                        oauthToken = oauthToken,
                        authorization = authorization
                    )
                    .execute()
                    .toAccessToken()
            }
        }
    }

    override suspend fun loadTwitterUser(scope: CoroutineScope): TwitterUserRecord? {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterUserDao.getAll().firstOrNull()
            }
        }
    }

    override suspend fun saveTwitterUser(record: TwitterUserRecord, scope: CoroutineScope) {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterUserDao.upsert(record)
            }
        }
    }

    override suspend fun deleteTwitterUser(user: TwitterUserRecord, scope: CoroutineScope): AccessTokenInvalidation? {
        return invalidateAccessToken(user, scope)
    }

    private suspend fun invalidateAccessToken(
        user: TwitterUserRecord,
        scope: CoroutineScope
    ): AccessTokenInvalidation? {
        val requestMetadata = TwitterRequestMetadata(
            method = TwitterEndpoints.invalidateTokenMethod,
            path = TwitterEndpoints.invalidateTokenPath,
            twitterConfig = twitterConfig
        )
        val authorization = TwitterSignature(
            requestMetadata = requestMetadata,
            twitterConfig = twitterConfig,
            base64Encoder = base64Encoder
        ).getOAuthValue(user)

        val result = invalidateAccessTokenToWeb(authorization, scope)
        if (!result.isSuccessful) {
            return null
        }
        return result.body()
    }

    private suspend fun invalidateAccessTokenToWeb(
        authorization: String,
        scope: CoroutineScope
    ): Response<AccessTokenInvalidation> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterApi.objectClient
                    .invalidateToken(
                        authorization = authorization
                    )
                    .execute()
            }
        }
    }

    private fun Response<String>.toAccessToken(): AccessToken {
        if (!this.isSuccessful) {
            return AccessToken(
                isValid = false,
                errorDescription = "network error. code: ${this.code()}"
            )
        }
        val brokenAccessToken = AccessToken(
            isValid = false,
            errorDescription = "broken response. code: 9998"
        )
        // Uriクラスのクエリストリングのパースを正常動作するために、ダミーのURLを結合させている
        val uri = Uri.parse("${TwitterEndpoints.baseEndpoint}?${this.body()}")
        return AccessToken(
            isValid = true,
            userId = uri.getQueryParameter("user_id") ?: return brokenAccessToken,
            oauthToken = uri.getQueryParameter("oauth_token") ?: return brokenAccessToken,
            oauthTokenSecret = uri.getQueryParameter("oauth_token_secret") ?: return brokenAccessToken,
            screenName = uri.getQueryParameter("screen_name") ?: return brokenAccessToken
        )
    }
}
