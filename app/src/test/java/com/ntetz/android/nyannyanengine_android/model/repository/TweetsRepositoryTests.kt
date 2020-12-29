package com.ntetz.android.nyannyanengine_android.model.repository

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApiEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.room.ICachedTweetsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.CachedTweetRecord
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TweetsRepositoryTests {
    @Mock
    private lateinit var mockTwitterApi: ITwitterApi

    @Mock
    private lateinit var mockTwitterConfig: ITwitterConfig

    @Mock
    private lateinit var mockCachedTweetsDao: ICachedTweetsDao

    @Test
    fun getTweets_キャッシュ非存在時Retrofitのレスポンス由来の値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse(
                    listOf(
                        Tweet(
                            id = 2828,
                            text = "dummyTextRepo",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        )
                    )
                )
            `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)

            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")
            `when`(mockCachedTweetsDao.getAll()).thenReturn(listOf())
            val testRepository =
                TweetsRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder,
                    cachedTweetsDao = mockCachedTweetsDao
                )
            Truth.assertThat(testRepository.getTweets(TwitterUserRecord("", "", "", ""), this))
                .isEqualTo(
                    listOf(
                        Tweet(
                            id = 2828,
                            text = "dummyTextRepo",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        )
                    )
                )
        }
    }

    @Test
    fun getTweets_キャッシュ非存在時Retrofitのレスポンス取得後キャッシュ用テーブル初期化が走ること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse(
                    listOf(
                        Tweet(
                            id = 2828,
                            text = "dummyTextRepo",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        )
                    )
                )
            `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)

            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")
            `when`(mockCachedTweetsDao.getAll()).thenReturn(listOf())
            val testRepository =
                TweetsRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder,
                    cachedTweetsDao = mockCachedTweetsDao
                )
            testRepository.getTweets(TwitterUserRecord("", "", "", ""), this)

            verify(mockCachedTweetsDao, times(1)).deleteAll()
        }
    }

    @Test
    fun getTweets_キャッシュ非存在時Retrofitのレスポンス取得後キャッシュ用テーブル更新が走ること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse(
                    listOf(
                        Tweet(
                            id = 2828,
                            text = "dummyTextRepo",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        )
                    )
                )
            `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)

            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")
            `when`(mockCachedTweetsDao.getAll()).thenReturn(listOf())
            val testRepository =
                TweetsRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder,
                    cachedTweetsDao = mockCachedTweetsDao
                )
            testRepository.getTweets(TwitterUserRecord("", "", "", ""), this)

            verify(mockCachedTweetsDao, times(1)).upsert(TestUtil.any())
        }
    }

    @Test
    fun getTweets_キャッシュ存在時Room内の値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            `when`(mockCachedTweetsDao.getAll()).thenReturn(
                listOf(
                    CachedTweetRecord(
                        id = 3838,
                        text = "dummyCachedTweet",
                        createdAt = "kinou",
                        userName = "cachedUser",
                        userScreenName = "cachedScreenUser",
                        profileImageUrlHttps = "https://ntetz.com/test.jpg"
                    )
                )
            )
            val testRepository =
                TweetsRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    cachedTweetsDao = mockCachedTweetsDao
                )
            Truth.assertThat(testRepository.getTweets(TwitterUserRecord("", "", "", ""), this))
                .isEqualTo(
                    listOf(
                        Tweet(
                            id = 3838,
                            text = "dummyCachedTweet",
                            createdAt = "kinou",
                            user = User("cachedUser", "cachedScreenUser", "https://ntetz.com/test.jpg")
                        )
                    )
                )
        }
    }

    @Test
    fun getLatestTweets_キャッシュ非存在時Retrofitのレスポンス由来の値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse(
                    listOf(
                        Tweet(
                            id = 2828,
                            text = "dummyTextRepo",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        )
                    )
                )
            `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)

            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")
            val testRepository =
                TweetsRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder,
                    cachedTweetsDao = mockCachedTweetsDao
                )
            Truth.assertThat(testRepository.getLatestTweets(TwitterUserRecord("", "", "", ""), this))
                .isEqualTo(
                    listOf(
                        Tweet(
                            id = 2828,
                            text = "dummyTextRepo",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        )
                    )
                )
        }
    }

    @Test
    fun getPreviousTweets_Retrofitのレスポンス由来の値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse(
                    listOf(
                        Tweet(
                            id = 2828,
                            text = "dummyTextRepoPrev",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        )
                    )
                )
            `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)

            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")
            val testRepository =
                TweetsRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder,
                    cachedTweetsDao = mockCachedTweetsDao
                )
            Truth.assertThat(
                testRepository.getPreviousTweets(
                    maxId = 1234567890123456789,
                    user = TwitterUserRecord("", "", "", ""),
                    this
                )
            )
                .isEqualTo(
                    listOf(
                        Tweet(
                            id = 2828,
                            text = "dummyTextRepoPrev",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        )
                    )
                )
        }
    }
}
