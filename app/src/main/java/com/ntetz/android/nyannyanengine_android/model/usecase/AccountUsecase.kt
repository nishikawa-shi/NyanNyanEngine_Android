package com.ntetz.android.nyannyanengine_android.model.usecase

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessToken
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessTokenInvalidation
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.NyanNyanUserComponent
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.SignInResultComponent
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import kotlinx.coroutines.CoroutineScope

interface IAccountUsecase {
    val nyanNyanUserEvent: LiveData<NyanNyanUserComponent?>
    val nyanNyanConfigEvent: LiveData<NyanNyanConfig?>

    suspend fun createAuthorizationEndpoint(scope: CoroutineScope): Uri?
    suspend fun fetchAccessToken(
        oauthVerifier: String,
        oauthToken: String,
        scope: CoroutineScope
    ): SignInResultComponent?

    suspend fun loadAccessToken(scope: CoroutineScope): TwitterUserRecord?
    suspend fun deleteAccessToken(scope: CoroutineScope): AccessTokenInvalidation?

    suspend fun fetchNyanNyanConfig()
    suspend fun fetchNyanNyanUser(twitterUser: TwitterUserRecord?)
}

class AccountUsecase(private val accountRepository: IAccountRepository) : IAccountUsecase {
    override val nyanNyanConfigEvent = accountRepository.nyanNyanConfigEvent
    override val nyanNyanUserEvent = Transformations.map(accountRepository.nyanNyanUserEvent) {
        NyanNyanUserComponent(
            nyanNyanUser = it ?: return@map null,
            nyanNyanConfig = nyanNyanConfigEvent.value ?: return@map null
        )
    }

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
        val tokenApiResult = accountRepository.getAccessToken(
            oauthVerifier = oauthVerifier,
            oauthToken = oauthToken,
            scope = scope
        )
        if (!tokenApiResult.isValid) {
            return SignInResultComponent(
                isSucceeded = false,
                errorMessage = tokenApiResult.errorDescription
            )
        }
        val verifyApiResult = accountRepository.verifyAccessToken(tokenApiResult, scope)
        val twitterUserRecord = createTwitterUserRecord(tokenApiResult, verifyApiResult)
            ?: return SignInResultComponent(
                isSucceeded = false,
                errorMessage = "shortage response. code: 9997"
            )
        runCatching { accountRepository.saveTwitterUser(twitterUserRecord, scope) }
            .onFailure {
                return SignInResultComponent(
                    isSucceeded = false,
                    errorMessage = "failed to save token. code: 9996"
                )
            }
        return SignInResultComponent(isSucceeded = true)
    }

    override suspend fun loadAccessToken(scope: CoroutineScope): TwitterUserRecord? {
        return accountRepository.loadTwitterUser(scope)
    }

    override suspend fun deleteAccessToken(scope: CoroutineScope): AccessTokenInvalidation? {
        val user = accountRepository.loadTwitterUser(scope) ?: return null
        return accountRepository.deleteTwitterUser(user, scope)
    }

    override suspend fun fetchNyanNyanConfig() {
        accountRepository.fetchNyanNyanConfig()
    }

    override suspend fun fetchNyanNyanUser(twitterUser: TwitterUserRecord?) {
        accountRepository.fetchNyanNyanUser(twitterUser)
    }

    private fun createTwitterUserRecord(tokenApiResponse: AccessToken, verifyApiResult: User): TwitterUserRecord? {
        return TwitterUserRecord(
            id = tokenApiResponse.id ?: return null,
            oauthToken = tokenApiResponse.oauthToken,
            oauthTokenSecret = tokenApiResponse.oauthTokenSecret,
            screenName = tokenApiResponse.screenName ?: return null,
            name = verifyApiResult.name,
            profileImageUrlHttps = verifyApiResult.profileImageUrlHttps
        )
    }
}
