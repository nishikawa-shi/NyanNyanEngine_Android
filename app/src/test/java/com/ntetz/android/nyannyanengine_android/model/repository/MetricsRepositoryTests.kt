package com.ntetz.android.nyannyanengine_android.model.repository

import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.AnalyticsEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MetricsRepositoryTests {
    @Mock
    private lateinit var mockFirebaseClient: IFirebaseClient

    @Test
    fun logEvent_firebaseClientのlogEventが一度呼ばれること() = runBlocking {
        val testStrMap = mapOf("testStr" to "testStrval")
        val testNumMap = mapOf("testInt" to 4)
        doNothing().`when`(mockFirebaseClient)
            .logEvent(AnalyticsEvent.TAP_TWEET, testStrMap, testNumMap)
        MetricsRepository(mockFirebaseClient).logEvent(
            AnalyticsEvent.TAP_TWEET,
            testStrMap,
            testNumMap,
            this
        )
        delay(20) // これがないと、内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間

        verify(mockFirebaseClient, times(1)).logEvent(
            AnalyticsEvent.TAP_TWEET,
            testStrMap,
            testNumMap
        )
    }
}
