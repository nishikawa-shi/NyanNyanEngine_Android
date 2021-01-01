package com.ntetz.android.nyannyanengine_android.model.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.ntetz.android.nyannyanengine_android.model.config.DefaultUserConfig
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.room.ITwitterUserDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanUser
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessToken
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessTokenInvalidation
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.IAccessToken
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterRequestMetadata
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignParam
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.TwitterSignature
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.util.Base64Encoder
import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface IAccountRepository {
    val nyanNyanConfigEvent: LiveData<NyanNyanConfig?>
    val nyanNyanUserEvent: LiveData<NyanNyanUser?>

    suspend fun getAuthorizationToken(scope: CoroutineScope): String?
    suspend fun getAccessToken(oauthVerifier: String, oauthToken: String, scope: CoroutineScope): AccessToken
    suspend fun verifyAccessToken(token: IAccessToken, scope: CoroutineScope): User
    suspend fun loadTwitterUser(scope: CoroutineScope): TwitterUserRecord?
    suspend fun saveTwitterUser(record: TwitterUserRecord, scope: CoroutineScope)
    suspend fun deleteTwitterUser(token: IAccessToken, scope: CoroutineScope): AccessTokenInvalidation?

    suspend fun fetchNyanNyanConfig()
    suspend fun fetchNyanNyanUser(twitterUser: TwitterUserRecord?)
    suspend fun incrementNekosanPoint(value: Int, twitterUser: TwitterUserRecord)
    suspend fun incrementTweetCount(twitterUser: TwitterUserRecord)
}

class AccountRepository(
    private val twitterApi: ITwitterApi,
    private val twitterConfig: ITwitterConfig = TwitterConfig(),
    private val base64Encoder: IBase64Encoder = Base64Encoder(),
    private val twitterUserDao: ITwitterUserDao,
    private val firebaseClient: IFirebaseClient
) : IAccountRepository {
    override val nyanNyanConfigEvent = firebaseClient.nyanNyanConfigEvent
    override val nyanNyanUserEvent = firebaseClient.nyanNyanUserEvent

    override suspend fun getAuthorizationToken(scope: CoroutineScope): String? {
        val additionalHeaders = listOf(
            TwitterSignParam(TwitterEndpoints.requestTokenOauthCallbackParamName, twitterConfig.callbackUrl)
        )
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
        return getRequestTokenFromWeb(authorization, scope).body()
    }

    override suspend fun getAccessToken(
        oauthVerifier: String,
        oauthToken: String,
        scope: CoroutineScope
    ): AccessToken {
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
        return getAccessTokenFromWeb(oauthVerifier, oauthToken, authorization, scope).toAccessToken()
    }

    override suspend fun verifyAccessToken(token: IAccessToken, scope: CoroutineScope): User {
        val requestMetadata = TwitterRequestMetadata(
            method = TwitterEndpoints.verifyCredentialsMethod,
            path = TwitterEndpoints.verifyCredentialsPath,
            twitterConfig = twitterConfig
        )
        val authorization = TwitterSignature(
            requestMetadata = requestMetadata,
            twitterConfig = twitterConfig,
            base64Encoder = base64Encoder
        ).getOAuthValue(token)

        val result = verifyAccessTokenToWeb(authorization, scope)
        val body = result.body()
        if (!result.isSuccessful || body == null) {
            return getErrorUser()
        }
        return body
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

    override suspend fun deleteTwitterUser(token: IAccessToken, scope: CoroutineScope): AccessTokenInvalidation? {
        deleteTwitterUserRecord(scope)
        return invalidateAccessToken(token, scope)
    }

    override suspend fun fetchNyanNyanConfig() {
        firebaseClient.fetchNyanNyanConfig()
    }

    override suspend fun fetchNyanNyanUser(twitterUser: TwitterUserRecord?) {
        firebaseClient.fetchNyanNyanUser(twitterUser)
    }

    override suspend fun incrementNekosanPoint(value: Int, twitterUser: TwitterUserRecord) {
        firebaseClient.incrementNyanNyanUser("np", value, twitterUser)
    }

    override suspend fun incrementTweetCount(twitterUser: TwitterUserRecord) {
        firebaseClient.incrementNyanNyanUser("tc", 1, twitterUser)
    }

    private suspend fun invalidateAccessToken(
        token: IAccessToken,
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
        ).getOAuthValue(token)

        val result = invalidateAccessTokenToWeb(authorization, scope)
        if (!result.isSuccessful) {
            return null
        }
        return result.body()
    }

    private suspend fun getRequestTokenFromWeb(
        authorization: String,
        scope: CoroutineScope
    ): Response<String> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterApi.scalarClient
                    .requestToken(authorization)
                    .execute()
            }
        }
    }

    private suspend fun getAccessTokenFromWeb(
        oauthVerifier: String,
        oauthToken: String,
        authorization: String,
        scope: CoroutineScope
    ): Response<String> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterApi.scalarClient
                    .accessToken(
                        oauthVerifier = oauthVerifier,
                        oauthToken = oauthToken,
                        authorization = authorization
                    )
                    .execute()
            }
        }
    }

    private suspend fun verifyAccessTokenToWeb(
        authorization: String,
        scope: CoroutineScope
    ): Response<User> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterApi.objectClient
                    .verifyCredentials(
                        authorization = authorization
                    )
                    .execute()
            }
        }
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

    private suspend fun deleteTwitterUserRecord(scope: CoroutineScope) {
        withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                twitterUserDao.deleteAll()
            }
        }
    }

    private fun getErrorUser(): User {
        return DefaultUserConfig.notSignInUser
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
            id = uri.getQueryParameter("user_id") ?: return brokenAccessToken,
            oauthToken = uri.getQueryParameter("oauth_token") ?: return brokenAccessToken,
            oauthTokenSecret = uri.getQueryParameter("oauth_token_secret") ?: return brokenAccessToken,
            screenName = uri.getQueryParameter("screen_name") ?: return brokenAccessToken
        )
    }
}
