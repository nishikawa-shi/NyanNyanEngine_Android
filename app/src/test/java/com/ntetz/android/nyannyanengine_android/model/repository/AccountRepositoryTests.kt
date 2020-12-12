package com.ntetz.android.nyannyanengine_android.model.repository

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApiEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.room.ITwitterUserDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AccountRepositoryTests {
    @Mock
    private lateinit var mockTwitterApi: ITwitterApi

    @Mock
    private lateinit var mockTwitterConfig: ITwitterConfig

    @Mock
    private lateinit var mockTwitterUserDao: ITwitterUserDao

    @Test
    fun getAuthorizationToken_Retrofitのレスポンス由来の値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse("mockResponseString")
            `when`(mockTwitterApi.client).thenReturn(mockEndpoints)

            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")
            `when`(mockTwitterConfig.callbackUrl).thenReturn("")
            val testRepository =
                AccountRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder,
                    twitterUserDao = mockTwitterUserDao
                )
            Truth.assertThat(testRepository.getAuthorizationToken(this))
                .isEqualTo("mockResponseString")
        }
    }

    @Test
    fun getAccessToken_Retrofitのレスポンス由来の値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse("mockTokenResponseString")
            `when`(mockTwitterApi.client).thenReturn(mockEndpoints)

            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")
            val testRepository =
                AccountRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder,
                    twitterUserDao = mockTwitterUserDao
                )
            Truth.assertThat(testRepository.getAccessToken("", "", this)!!.body())
                .isEqualTo("mockTokenResponseString")
        }
    }

    @Test
    fun loadTwitterUser_daoのgetAll由来の値が取得できること() = runBlocking {
        `when`(mockTwitterUserDao.getAll()).thenReturn(
            listOf(
                TwitterUserRecord(
                    "iDdum1",
                    oauthToken = "tokDum1",
                    oauthTokenSecret = "secDum1",
                    screenName = "scDum1"
                )
            )
        )

        Truth.assertThat(
            AccountRepository(
                twitterApi = mockTwitterApi,
                twitterConfig = mockTwitterConfig,
                base64Encoder = TestUtil.mockBase64Encoder,
                twitterUserDao = mockTwitterUserDao
            ).loadTwitterUser(this)
        )
            .isEqualTo(
                TwitterUserRecord(
                    "iDdum1",
                    oauthToken = "tokDum1",
                    oauthTokenSecret = "secDum1",
                    screenName = "scDum1"
                )
            )
    }

    @Test
    fun saveTwitterUser_twitterUserDaoのupsertが1度実行されること() = runBlocking {
        val testTwitterUserRecord = TwitterUserRecord(
            "iDdum2",
            oauthToken = "tokDum2",
            oauthTokenSecret = "secDum2",
            screenName = "scDum2"
        )
        Mockito.doNothing().`when`(mockTwitterUserDao).upsert(testTwitterUserRecord)

        AccountRepository(
            twitterApi = mockTwitterApi,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder,
            twitterUserDao = mockTwitterUserDao
        ).saveTwitterUser(testTwitterUserRecord, this)
        delay(20) // これがないと、initialize内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間

        Mockito.verify(mockTwitterUserDao, Mockito.times(1)).upsert(testTwitterUserRecord)
    }
}
