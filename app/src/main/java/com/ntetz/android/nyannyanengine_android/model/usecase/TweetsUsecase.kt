package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.config.DefaultTweetConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import com.ntetz.android.nyannyanengine_android.model.repository.ITweetsRepository
import kotlinx.coroutines.CoroutineScope

interface ITweetsUsecase {
    suspend fun getTweets(scope: CoroutineScope): List<Tweet>?
}

class TweetsUsecase(
    private val tweetsRepository: ITweetsRepository,
    private val accountRepository: IAccountRepository
) : ITweetsUsecase {
    override suspend fun getTweets(scope: CoroutineScope): List<Tweet>? {
        val user = accountRepository.loadTwitterUser(scope) ?: return DefaultTweetConfig.notSignInlist
        val result = tweetsRepository.getTweets(user = user, scope = scope)
        if (result?.isSuccessful != true) {
            return when (result?.code()) {
                429 -> DefaultTweetConfig.tooManyRequestList
                else -> DefaultTweetConfig.undefinedErrorList
            }
        }
        return result.body()
    }
}
