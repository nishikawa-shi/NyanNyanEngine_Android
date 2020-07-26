package com.ntetz.android.nyannyanengine_android.model.repository

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApiEndpoints
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

@RunWith(MockitoJUnitRunner::class)
class AccountRepositoryTests {
    @Mock
    private lateinit var mockTwitterApi: ITwitterApi

    @Test
    fun getAuthorizationToken_Retrofitのレスポンス由来の値が得られること() = runBlocking {
        withContext(Dispatchers.IO) {
            val retrofit = Retrofit
                .Builder()
                .baseUrl(TwitterEndpoints.baseEndpoint)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
            val behavior = NetworkBehavior.create().also {
                it.setDelay(0, TimeUnit.MILLISECONDS)
                it.setFailurePercent(0)
                it.setErrorPercent(0)
            }
            val mockEndpoints = MockRetrofit
                .Builder(retrofit)
                .networkBehavior(behavior)
                .build()
                .create(ITwitterApiEndpoints::class.java)
                .returningResponse("mockResponseString")
            `when`(mockTwitterApi.client).thenReturn(mockEndpoints)

            val testRepository =
                AccountRepository(twitterApi = mockTwitterApi, base64Encoder = TestUtil.mockBase64Encoder)
            Truth.assertThat(testRepository.getAuthorizationToken(this))
                .isEqualTo("mockResponseString")
        }
    }
}
