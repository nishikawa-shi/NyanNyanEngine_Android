package com.ntetz.android.nyannyanengine_android.ui.main

import androidx.paging.PagingSource
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TweetsPagingSourceTests {
    @Mock
    private lateinit var mockTweetUsecase: ITweetsUsecase

    @Test
    fun load_初読み込みかつレスポンスありの時レスポンスを返す() = runBlocking {
        val mockTweets = listOf(
            Tweet(
                id = 2828,
                text = "dummyUsCsNom",
                createdAt = "3 gatsu 2 nichi",
                user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
            )
        )
        `when`(mockTweetUsecase.getLatestTweets(this)).thenReturn(mockTweets)
        val testLoadParam =
            PagingSource.LoadParams.Refresh<Long>(key = null, loadSize = 99, placeholdersEnabled = false)

        val testSource = TweetsPagingSource(mockTweetUsecase, this)
        Truth.assertThat(testSource.load(testLoadParam))
            .isEqualTo(
                PagingSource.LoadResult.Page<Long, Tweet>(
                    data = mockTweets,
                    prevKey = null,
                    nextKey = 2828
                )
            )
        return@runBlocking
    }

    @Test
    fun load_初読み込みかつエラーレスポンスの時次ページ指定のないページを返す() = runBlocking {
        val mockTweets = listOf(
            Tweet(
                id = 28,
                text = "dummyUsCsNomError",
                createdAt = "3 gatsu 2 nichi",
                user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
            )
        )
        `when`(mockTweetUsecase.getLatestTweets(this)).thenReturn(mockTweets)
        val testLoadParam =
            PagingSource.LoadParams.Refresh<Long>(key = null, loadSize = 99, placeholdersEnabled = false)

        val testSource = TweetsPagingSource(mockTweetUsecase, this)
        Truth.assertThat(testSource.load(testLoadParam))
            .isEqualTo(
                PagingSource.LoadResult.Page<Long, Tweet>(
                    data = mockTweets,
                    prevKey = null,
                    nextKey = null
                )
            )
        return@runBlocking
    }

    @Test
    fun load_2ページ目以降の読み込みかつレスポンスありの時レスポンスを返す() = runBlocking {
        val mockTweets = listOf(
            Tweet(
                id = 2828,
                text = "dummyUsCsNom",
                createdAt = "3 gatsu 2 nichi",
                user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
            ),
            Tweet(
                id = 2825,
                text = "dummyUsCsNom",
                createdAt = "3 gatsu 2 nichi",
                user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
            )
        )
        `when`(mockTweetUsecase.getPreviousTweets(2828, this)).thenReturn(mockTweets)
        val testLoadParam =
            PagingSource.LoadParams.Refresh<Long>(key = 2828, loadSize = 99, placeholdersEnabled = false)

        val testSource = TweetsPagingSource(mockTweetUsecase, this)
        Truth.assertThat(testSource.load(testLoadParam))
            .isEqualTo(
                PagingSource.LoadResult.Page<Long, Tweet>(
                    data = mockTweets,
                    prevKey = 2828,
                    nextKey = 2825
                )
            )
        return@runBlocking
    }

    @Test
    fun load_2ページ目以降の読み込みかつエラーレスポンスの次ページ指定のないページを返す() = runBlocking {
        val mockTweets = listOf(
            Tweet(
                id = 28,
                text = "dummyUsCsNomError",
                createdAt = "3 gatsu 2 nichi",
                user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
            )
        )
        `when`(mockTweetUsecase.getPreviousTweets(2828, this)).thenReturn(mockTweets)
        val testLoadParam =
            PagingSource.LoadParams.Refresh<Long>(key = 2828, loadSize = 99, placeholdersEnabled = false)

        val testSource = TweetsPagingSource(mockTweetUsecase, this)
        Truth.assertThat(testSource.load(testLoadParam))
            .isEqualTo(
                PagingSource.LoadResult.Page<Long, Tweet>(
                    data = mockTweets,
                    prevKey = 2828,
                    nextKey = null
                )
            )
        return@runBlocking
    }
}
