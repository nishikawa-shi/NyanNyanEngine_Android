package com.ntetz.android.nyannyanengine_android

import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import java.util.*
import java.util.concurrent.TimeUnit
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

class TestUtil private constructor() {

    companion object {
        val mockBase64Encoder: IBase64Encoder = MockBase64Encoder()

        fun <T> any(): T {
            Mockito.any<T>()
            return nullReturn()
        }

        val mockNormalRetrofit: MockRetrofit
            get() {
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
                return MockRetrofit
                    .Builder(retrofit)
                    .networkBehavior(behavior)
                    .build()
            }

        val mockErrorRetrofit: MockRetrofit
            get() {
                val retrofit = Retrofit
                    .Builder()
                    .baseUrl(TwitterEndpoints.baseEndpoint)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
                val behavior = NetworkBehavior.create().also {
                    it.setDelay(0, TimeUnit.MILLISECONDS)
                    it.setErrorPercent(100)
                }
                return MockRetrofit
                    .Builder(retrofit)
                    .networkBehavior(behavior)
                    .build()
            }

        private fun <T> nullReturn(): T = null as T
    }
}

private class MockBase64Encoder : IBase64Encoder {
    override fun encodeToStringForHttp(byteArray: ByteArray): String {
        val encoder = Base64.getUrlEncoder()
        return encoder.encodeToString(byteArray)
    }
}
