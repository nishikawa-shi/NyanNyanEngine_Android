package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ntetz.android.nyannyanengine_android.model.config.DefaultUserConfig
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

    suspend fun createAuthorizationEndpoint(scope: CoroutineScope, context: Context): Uri?
    suspend fun fetchAccessToken(
        oauthVerifier: String,
        oauthToken: String,
        scope: CoroutineScope,
        context: Context
    ): SignInResultComponent?

    suspend fun loadAccessToken(scope: CoroutineScope, context: Context): TwitterUserRecord
    suspend fun deleteAccessToken(scope: CoroutineScope, context: Context): AccessTokenInvalidation?

    suspend fun fetchNyanNyanConfig()
    suspend fun fetchNyanNyanUser(twitterUser: TwitterUserRecord, context: Context)
}

class AccountUsecase(private val accountRepository: IAccountRepository) : IAccountUsecase {
    override val nyanNyanConfigEvent = accountRepository.nyanNyanConfigEvent
    override val nyanNyanUserEvent = Transformations.map(accountRepository.nyanNyanUserEvent) {
        NyanNyanUserComponent(
            nyanNyanUser = it ?: return@map null,
            nyanNyanConfig = nyanNyanConfigEvent.value ?: return@map null
        )
    }

    override suspend fun createAuthorizationEndpoint(scope: CoroutineScope, context: Context): Uri? {
        val urlStr = listOf(
            TwitterEndpoints.baseEndpoint,
            TwitterEndpoints.authorizePagePath
        ).joinToString("")
        val queryStr = accountRepository.getAuthorizationToken(scope, context) ?: return null
        return Uri.parse("$urlStr?$queryStr")
    }

    override suspend fun fetchAccessToken(
        oauthVerifier: String,
        oauthToken: String,
        scope: CoroutineScope,
        context: Context
    ): SignInResultComponent {
        val tokenApiResult = accountRepository.getAccessToken(
            oauthVerifier = oauthVerifier,
            oauthToken = oauthToken,
            scope = scope,
            context = context
        )
        if (!tokenApiResult.isValid) {
            return SignInResultComponent(
                isSucceeded = false,
                errorMessage = tokenApiResult.errorDescription
            )
        }
        val verifyApiResult = accountRepository.verifyAccessToken(tokenApiResult, scope, context)
        val twitterUserRecord = createTwitterUserRecord(tokenApiResult, verifyApiResult, context)
        runCatching { accountRepository.saveTwitterUser(twitterUserRecord, scope) }
            .onFailure {
                return SignInResultComponent(
                    isSucceeded = false,
                    errorMessage = "failed to save token. code: 9996"
                )
            }
        return SignInResultComponent(isSucceeded = true)
    }

    override suspend fun loadAccessToken(scope: CoroutineScope, context: Context): TwitterUserRecord {
        return accountRepository.loadTwitterUser(scope) ?: DefaultUserConfig.getNotSignInUser(context)
    }

    override suspend fun deleteAccessToken(scope: CoroutineScope, context: Context): AccessTokenInvalidation? {
        val user = accountRepository.loadTwitterUser(scope) ?: return null
        return accountRepository.deleteTwitterUser(user, scope, context)
    }

    override suspend fun fetchNyanNyanConfig() {
        accountRepository.fetchNyanNyanConfig()
    }

    override suspend fun fetchNyanNyanUser(twitterUser: TwitterUserRecord, context: Context) {
        accountRepository.fetchNyanNyanUser(twitterUser, context)
    }

    private fun createTwitterUserRecord(
        tokenApiResponse: AccessToken,
        verifyApiResult: User? = null,
        context: Context
    ): TwitterUserRecord {
        verifyApiResult ?: return DefaultUserConfig.getNotSignInUser(context)
        return TwitterUserRecord(
            id = tokenApiResponse.id ?: return DefaultUserConfig.getNotSignInUser(context),
            oauthToken = tokenApiResponse.oauthToken,
            oauthTokenSecret = tokenApiResponse.oauthTokenSecret,
            screenName = tokenApiResponse.screenName ?: return DefaultUserConfig.getNotSignInUser(context),
            name = verifyApiResult.name,
            profileImageUrlHttps = verifyApiResult.profileImageUrlHttps
        )
    }
}
