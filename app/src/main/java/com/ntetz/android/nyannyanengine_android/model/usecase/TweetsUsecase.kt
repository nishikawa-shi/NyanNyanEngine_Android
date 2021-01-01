package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.config.DefaultTweetConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import com.ntetz.android.nyannyanengine_android.model.repository.ITweetsRepository
import kotlinx.coroutines.CoroutineScope

interface ITweetsUsecase {
    suspend fun getLatestTweets(scope: CoroutineScope): List<Tweet>
    suspend fun getPreviousTweets(maxId: Long, scope: CoroutineScope): List<Tweet>
    suspend fun postTweet(tweetBody: String, scope: CoroutineScope): Tweet
}

class TweetsUsecase(
    private val tweetsRepository: ITweetsRepository,
    private val accountRepository: IAccountRepository
) : ITweetsUsecase {
    override suspend fun getLatestTweets(scope: CoroutineScope): List<Tweet> {
        val user = accountRepository.loadTwitterUser(scope) ?: return DefaultTweetConfig.notSignInlist
        return tweetsRepository.getLatestTweets(token = user, scope = scope)
    }

    override suspend fun getPreviousTweets(maxId: Long, scope: CoroutineScope): List<Tweet> {
        val user = accountRepository.loadTwitterUser(scope) ?: return DefaultTweetConfig.notSignInlist
        return tweetsRepository.getPreviousTweets(maxId = maxId, token = user, scope = scope)
    }

    override suspend fun postTweet(tweetBody: String, scope: CoroutineScope): Tweet {
        val user = accountRepository.loadTwitterUser(scope) ?: return DefaultTweetConfig.notSignInlist[0]
        accountRepository.incrementNekosanPoint(30, user)
        accountRepository.incrementTweetCount(user)
        return tweetsRepository.postTweet(tweetBody, user, scope)
    }
}
