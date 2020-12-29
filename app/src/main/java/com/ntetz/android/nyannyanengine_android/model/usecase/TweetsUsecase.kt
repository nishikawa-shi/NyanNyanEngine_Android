package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.config.DefaultTweetConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import com.ntetz.android.nyannyanengine_android.model.repository.ITweetsRepository
import kotlinx.coroutines.CoroutineScope

interface ITweetsUsecase {
    suspend fun getLatestTweets(scope: CoroutineScope): List<Tweet>
    suspend fun getPreviousTweets(maxId: Long, scope: CoroutineScope): List<Tweet>
}

class TweetsUsecase(
    private val tweetsRepository: ITweetsRepository,
    private val accountRepository: IAccountRepository
) : ITweetsUsecase {
    override suspend fun getLatestTweets(scope: CoroutineScope): List<Tweet> {
        val user = accountRepository.loadTwitterUser(scope) ?: return DefaultTweetConfig.notSignInlist
        return tweetsRepository.getLatestTweets(user = user, scope = scope)
    }

    override suspend fun getPreviousTweets(maxId: Long, scope: CoroutineScope): List<Tweet> {
        val user = accountRepository.loadTwitterUser(scope) ?: return DefaultTweetConfig.notSignInlist
        return tweetsRepository.getPreviousTweets(maxId = maxId, user = user, scope = scope)
    }
}
