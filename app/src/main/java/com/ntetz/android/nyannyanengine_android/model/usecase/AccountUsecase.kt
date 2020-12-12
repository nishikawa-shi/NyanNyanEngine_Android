package com.ntetz.android.nyannyanengine_android.model.usecase

import android.net.Uri
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.SignInResultComponent
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import kotlinx.coroutines.CoroutineScope

interface IAccountUsecase {
    suspend fun createAuthorizationEndpoint(scope: CoroutineScope): Uri?
    suspend fun fetchAccessToken(
        oauthVerifier: String,
        oauthToken: String,
        scope: CoroutineScope
    ): SignInResultComponent?
}

class AccountUsecase(private val accountRepository: IAccountRepository) : IAccountUsecase {
    override suspend fun createAuthorizationEndpoint(scope: CoroutineScope): Uri? {
        val urlStr = listOf(
            TwitterEndpoints.baseEndpoint,
            TwitterEndpoints.authorizePagePath
        ).joinToString("")
        val queryStr = accountRepository.getAuthorizationToken(scope) ?: return null
        return Uri.parse("$urlStr?$queryStr")
    }

    override suspend fun fetchAccessToken(
        oauthVerifier: String,
        oauthToken: String,
        scope: CoroutineScope
    ): SignInResultComponent {
        val token =
            accountRepository.getAccessToken(
                oauthVerifier = oauthVerifier,
                oauthToken = oauthToken,
                scope = scope
            ) ?: return SignInResultComponent(isSucceeded = false, errorCode = 9999, errorMessage = "no response...")
        if (!token.isSuccessful) {
            return SignInResultComponent(
                isSucceeded = false,
                errorCode = token.code(),
                errorMessage = "network error code ${token.code()}"
            )
        }
        val twitterUserRecord = createTwitterUserRecord(token.body()) ?: return SignInResultComponent(
            isSucceeded = false,
            errorCode = 9998,
            errorMessage = "broken response..."
        )
        kotlin.runCatching { accountRepository.saveTwitterUser(twitterUserRecord, scope) }.onFailure {
            return SignInResultComponent(
                isSucceeded = false,
                errorCode = 9997,
                errorMessage = "failed to save token..."
            )
        }
        return SignInResultComponent(isSucceeded = true)
    }

    private fun createTwitterUserRecord(tokenApiResponse: String?): TwitterUserRecord? {
        // Uriクラスのクエリストリングのパースを正常動作するために、ダミーのURLを結合させている
        val uri = Uri.parse("${TwitterEndpoints.baseEndpoint}?$tokenApiResponse") ?: return null
        return TwitterUserRecord(
            id = uri.getQueryParameter("user_id") ?: return null,
            oauthToken = uri.getQueryParameter("oauth_token") ?: return null,
            oauthTokenSecret = uri.getQueryParameter("oauth_token_secret") ?: return null,
            screenName = uri.getQueryParameter("screen_name") ?: return null
        )
    }
}
