package com.ntetz.android.nyannyanengine_android.model.usecase

import android.net.Uri
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
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
        return SignInResultComponent(isSucceeded = true)
    }
}
