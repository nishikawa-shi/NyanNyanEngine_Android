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
    fun getTweets_Retrofitのレスポンス由来の値が得られること() = runBlocking {
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
            Truth.assertThat(testRepository.getTweets(TwitterUserRecord("", "", "", ""), this)!!)
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
}
