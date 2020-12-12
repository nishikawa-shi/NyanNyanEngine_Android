package com.ntetz.android.nyannyanengine_android.model.repository

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApiEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.room.ITwitterUserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
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
}
