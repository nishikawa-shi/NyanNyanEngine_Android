package com.ntetz.android.nyannyanengine_android.ui.main

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import kotlinx.coroutines.CoroutineScope

class TweetsPagingSource(
    private val tweetsUsecase: ITweetsUsecase,
    private val scope: CoroutineScope,
    private val context: Context
) : PagingSource<Long, Tweet>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Tweet> {
        val maxId = params.key

        if (maxId == null) {
            val tweets = tweetsUsecase.getLatestTweets(scope, context)
            val prevKey = null
            val nextKey = if (tweets[0].isError) null else tweets.last().id
            return LoadResult.Page(
                data = tweets,
                prevKey = prevKey,
                nextKey = nextKey
            )
        }

        return try {
            val tweets = tweetsUsecase.getPreviousTweets(maxId, scope, context)
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

    override fun getRefreshKey(state: PagingState<Long, Tweet>): Long? = state.anchorPosition?.toLong()
}
