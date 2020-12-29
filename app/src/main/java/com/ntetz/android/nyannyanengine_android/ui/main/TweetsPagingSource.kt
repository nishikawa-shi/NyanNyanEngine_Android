package com.ntetz.android.nyannyanengine_android.ui.main

import androidx.paging.PagingSource
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import kotlinx.coroutines.CoroutineScope

class TweetsPagingSource(
    private val tweetsUsecase: ITweetsUsecase,
    private val scope: CoroutineScope
) : PagingSource<Long, Tweet>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Tweet> {
        val maxId = params.key
        if (maxId == null) {
            val tweets = tweetsUsecase.getTweets(scope)
            return LoadResult.Page(
                data = tweets,
                prevKey = null,
                nextKey = tweets.last().id
            )
        }

        return try {
            val tweets = tweetsUsecase.getPreviousTweets(maxId, scope)
            val prevKey = if (tweets[0].isError) maxId else tweets.first().id
            val nextKey = if (tweets[0].isError) null else tweets.last().id
            LoadResult.Page(
                data = tweets,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override val keyReuseSupported: Boolean
        get() = true
}
