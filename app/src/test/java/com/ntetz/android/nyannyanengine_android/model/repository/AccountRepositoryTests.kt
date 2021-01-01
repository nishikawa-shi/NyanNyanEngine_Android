package com.ntetz.android.nyannyanengine_android.model.repository

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApiEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.room.ITwitterUserDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessToken
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessTokenInvalidation
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
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
            `when`(mockTwitterApi.scalarClient).thenReturn(mockEndpoints)

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
            // 通常レスポンスだと、android.Uriクラスを利用してしまうためエラーレスポンスとしている。
            val mockEndpoints = TestUtil.mockErrorRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse("error!")

            `when`(mockTwitterApi.scalarClient).thenReturn(mockEndpoints)
            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")

            val testRepository =
                AccountRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder,
                    twitterUserDao = mockTwitterUserDao
                )
            Truth.assertThat(testRepository.getAccessToken("", "", this).errorDescription)
                .isEqualTo("network error. code: 500")
        }
    }

    @Test
    fun verifyAccessToken_Retrofitのレスポンス由来の値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            val mockEndpoints = TestUtil.mockNormalRetrofit
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse(User(name = "testUser", screenName = "testScName"))

            `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)
            `when`(mockTwitterConfig.apiSecret).thenReturn("")
            `when`(mockTwitterConfig.consumerKey).thenReturn("")

            val testRepository =
                AccountRepository(
                    twitterApi = mockTwitterApi,
                    twitterConfig = mockTwitterConfig,
                    base64Encoder = TestUtil.mockBase64Encoder,
                    twitterUserDao = mockTwitterUserDao
                )
            Truth.assertThat(
                testRepository.verifyAccessToken(
                    AccessToken(true, null, null, "", "", null),
                    this
                )
            ).isEqualTo(User(name = "testUser", screenName = "testScName"))
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
                    screenName = "scDum1",
                    name = "testName",
                    profileImageUrlHttps = null
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
                    screenName = "scDum1",
                    name = "testName",
                    profileImageUrlHttps = null
                )
            )
    }

    @Test
    fun saveTwitterUser_twitterUserDaoのupsertが1度実行されること() = runBlocking {
        val testTwitterUserRecord = TwitterUserRecord(
            "iDdum2",
            oauthToken = "tokDum2",
            oauthTokenSecret = "secDum2",
            screenName = "scDum2",
            name = "testName",
            profileImageUrlHttps = null
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

    @Test
    fun deleteTwitterUser_Retrofitのレスポンス由来の値が得られること() = runBlocking {
        val mockEndpoints = TestUtil.mockNormalRetrofit
            .create(ITwitterApiEndpoints::class.java)
            .returningResponse(AccessTokenInvalidation("dummyToken"))
        `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)
        `when`(mockTwitterConfig.apiSecret).thenReturn("")
        `when`(mockTwitterConfig.consumerKey).thenReturn("")

        val testRepository =
            AccountRepository(
                twitterApi = mockTwitterApi,
                twitterConfig = mockTwitterConfig,
                base64Encoder = TestUtil.mockBase64Encoder,
                twitterUserDao = mockTwitterUserDao
            )
        Truth.assertThat(
            testRepository.deleteTwitterUser(
                TwitterUserRecord(
                    "du",
                    "testToken",
                    "testSecret",
                    "testScNm",
                    name = "testName",
                    profileImageUrlHttps = null
                ), this
            )
        ).isEqualTo(AccessTokenInvalidation("dummyToken"))
    }

    @Test
    fun deleteTwitterUser_twitterUserDaoのdeleteAllが1度実行されること() = runBlocking {
        val mockEndpoints = TestUtil.mockNormalRetrofit
            .create(ITwitterApiEndpoints::class.java)
            .returningResponse(AccessTokenInvalidation("dummyToken"))
        `when`(mockTwitterApi.objectClient).thenReturn(mockEndpoints)
        `when`(mockTwitterConfig.apiSecret).thenReturn("")
        `when`(mockTwitterConfig.consumerKey).thenReturn("")

        AccountRepository(
            twitterApi = mockTwitterApi,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder,
            twitterUserDao = mockTwitterUserDao
        ).deleteTwitterUser(
            TwitterUserRecord(
                "du",
                "testToken",
                "testSecret",
                "testScNm",
                name = "testName",
                profileImageUrlHttps = null
            ), this
        )
        delay(20) // これがないと、initialize内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間

        Mockito.verify(mockTwitterUserDao, Mockito.times(1)).deleteAll()
    }
}
