package com.ntetz.android.nyannyanengine_android.model.usecase

import android.net.Uri
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import kotlinx.coroutines.CoroutineScope

interface IAccountUsecase {
    suspend fun createAuthorizationEndpoint(scope: CoroutineScope): Uri?
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
}
