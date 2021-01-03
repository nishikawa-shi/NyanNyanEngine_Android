package com.ntetz.android.nyannyanengine_android.model.repository

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApiEndpoints
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TweetsRepositoryTests {
    @Mock
    private lateinit var mockTwitterApi: ITwitterApi

    @Mock
    private lateinit var mockTwitterConfig: ITwitterConfig

    @Test
    fun getLatestTweets_Retrofitのレスポンス由来の値が得られること() = runBlocking {
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
                    base64Encoder = TestUtil.mockBase64Encoder
                )
            Truth.assertThat(
                testRepository.getLatestTweets(
                    TwitterUserRecord(
                        "", "", "", "", "testName", null
                    ), this
                )
            )
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
    fun getLatestTweets_Retrofitのレスポンスがidで降順ソートされていること() = runBlocking {
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
                        ),
                        Tweet(
                            id = 2830,
                            text = "dummyTextRepo",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        ),
                        Tweet(
                            id = 2829,
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
                    base64Encoder = TestUtil.mockBase64Encoder
                )
            Truth.assertThat(
                testRepository.getLatestTweets(
                    TwitterUserRecord(
                        "", "", "", "", "testName", null
                    ), this
                )
            )
                .isEqualTo(
                    listOf(
                        Tweet(
                            id = 2830,
                            text = "dummyTextRepo",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        ),
                        Tweet(
                            id = 2829,
                            text = "dummyTextRepo",
                            createdAt = "3 gatsu 2 nichi",
                            user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                        ),
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
                    base64Encoder = TestUtil.mockBase64Encoder
                )
            Truth.assertThat(
                testRepository.getPreviousTweets(
                    maxId = 1234567890123456789,
                    token = TwitterUserRecord(
                        "", "", "", "", "testName", null
                    ),
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

    @Test
    fun postTweet_Retrofitのレスポンス由来の値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse(
                    Tweet(
                        id = 2828,
                        text = "postTestTweeter!",
                        createdAt = "3 gatsu 2 nichi",
                        user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                    )
                )
            `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)

            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")
            val testRepository =
                TweetsRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder
                )
            Truth.assertThat(
                testRepository.postTweet(
                    tweetBody = "dummy",
                    point = 300,
                    token = TwitterUserRecord(
                        "", "", "", "", "testName", null
                    ),
                    scope = this
                )
            )
                .isEqualTo(
                    Tweet(
                        id = 2828,
                        text = "postTestTweeter!",
                        createdAt = "3 gatsu 2 nichi",
                        user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                    )
                )
        }
    }

    @Test
    fun postTweet_pointの値が反映された値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse(
                    Tweet(
                        id = 2828,
                        text = "postTestTweeter!",
                        createdAt = "3 gatsu 2 nichi",
                        user = User("dummyTextRepoName", "dummyTextRepoScNm", "https://ntetz.com/dummyTextRepo.jpg")
                    )
                )
            `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)

            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")
            val testRepository =
                TweetsRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder
                )
            Truth.assertThat(
                testRepository.postTweet(
                    tweetBody = "dummy",
                    point = 300,
                    token = TwitterUserRecord(
                        "", "", "", "", "testName", null
                    ),
                    scope = this
                ).point
            ).isEqualTo(300)
        }
    }
}
