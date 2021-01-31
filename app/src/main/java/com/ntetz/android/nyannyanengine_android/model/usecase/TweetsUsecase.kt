package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.config.DefaultTweetConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import com.ntetz.android.nyannyanengine_android.model.repository.IHashtagsRepository
import com.ntetz.android.nyannyanengine_android.model.repository.ITweetsRepository
import com.ntetz.android.nyannyanengine_android.util.nekosanPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

interface ITweetsUsecase {
    suspend fun getLatestTweets(scope: CoroutineScope, context: Context): List<Tweet>
    suspend fun getPreviousTweets(maxId: Long, scope: CoroutineScope, context: Context): List<Tweet>
    suspend fun postTweet(tweetBody: String, scope: CoroutineScope, context: Context): Tweet
}

class TweetsUsecase @Inject constructor(
    private val tweetsRepository: ITweetsRepository,
    private val accountRepository: IAccountRepository,
    private val hashtagsRepository: IHashtagsRepository
) : ITweetsUsecase {
    override suspend fun getLatestTweets(scope: CoroutineScope, context: Context): List<Tweet> {
        val user = accountRepository.loadTwitterUser(scope) ?: return DefaultTweetConfig.getNotSignInlist(context)
        return tweetsRepository.getLatestTweets(token = user, scope = scope, context = context)
    }

    override suspend fun getPreviousTweets(maxId: Long, scope: CoroutineScope, context: Context): List<Tweet> {
        val user = accountRepository.loadTwitterUser(scope) ?: return DefaultTweetConfig.getNotSignInlist(context)
        return tweetsRepository.getPreviousTweets(maxId = maxId, token = user, scope = scope, context = context)
    }

    override suspend fun postTweet(tweetBody: String, scope: CoroutineScope, context: Context): Tweet {
        val user = accountRepository.loadTwitterUser(scope) ?: return DefaultTweetConfig.getNotSignInlist(context)[0]
        val hashtags = hashtagsRepository.getDefaultHashtags(scope, context)
        val totalPoint = getTweetNekosanPoint(tweetBody, hashtags)

        accountRepository.incrementNekosanPoint(totalPoint, user)
        accountRepository.incrementTweetCount(user)
        return tweetsRepository.postTweet(
            getTweetBodyWithHashtags(tweetBody, hashtags),
            totalPoint,
            user,
            scope,
            context
        )
    }

    private fun getTweetBodyWithHashtags(tweetBody: String, hashtags: List<DefaultHashTagComponent>): String {
        return hashtags.filter { it.isEnabled }.fold(tweetBody) { result, current ->
            listOf(result, current.textBody).joinToString("\n")
        }
    }

    private fun getTweetNekosanPoint(tweetBody: String, hashtags: List<DefaultHashTagComponent>): Int {
        val multipliter = accountRepository.nyanNyanConfigEvent.value?.nekosanPointMultiplier ?: 1
        val rawPoint = tweetBody.nekosanPoint() + hashtags.fold(0) { result, current -> result + current.nekosanPoint }
        return rawPoint * multipliter
    }
}
