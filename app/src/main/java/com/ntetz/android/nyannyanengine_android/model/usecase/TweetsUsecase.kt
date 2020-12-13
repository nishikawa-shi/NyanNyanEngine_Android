package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import com.ntetz.android.nyannyanengine_android.model.repository.ITweetsRepository
import kotlinx.coroutines.CoroutineScope

interface ITweetsUsecase {
    suspend fun getTweets(scope: CoroutineScope): String?
}

class TweetsUsecase(
    private val tweetsRepository: ITweetsRepository,
    private val accountRepository: IAccountRepository
) : ITweetsUsecase {
    override suspend fun getTweets(scope: CoroutineScope): String? {
        val user = accountRepository.loadTwitterUser(scope) ?: return null
        val result = tweetsRepository.getTweets(user = user, scope = scope)
        return result?.body()
    }
}
