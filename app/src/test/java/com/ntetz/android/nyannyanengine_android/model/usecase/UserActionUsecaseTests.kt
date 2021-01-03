package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.AnalyticsEvent
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.screen_transition.UserAction
import com.ntetz.android.nyannyanengine_android.model.repository.IMetricsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserActionUsecaseTests {
    @Mock
    private lateinit var mockMetricsRepository: IMetricsRepository

    @Test
    fun tap_metricsRepositoryのlogEventが呼ばれること() = runBlocking {
        val testStrMap = mapOf("testStr" to "testStrval")
        val testNumMap = mapOf("testInt" to 4)
        `when`(mockMetricsRepository.logEvent(AnalyticsEvent.TAP_TWEET, testStrMap, testNumMap, this)).thenReturn(null)

        UserActionUsecase(mockMetricsRepository).tap(UserAction.TWEET, testStrMap, testNumMap, this)
        delay(20) // これがないと、内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間
        verify(mockMetricsRepository, times(1)).logEvent(
            AnalyticsEvent.TAP_TWEET,
            testStrMap,
            testNumMap,
            this
        )
    }

    @Test
    fun tap_渡す行動種別に応じた値でlogEventが呼ばれること() = runBlocking {
        val testStrMap = mapOf("testStr" to "testStrval")
        val testNumMap = mapOf("testInt" to 4)
        `when`(mockMetricsRepository.logEvent(AnalyticsEvent.TAP_POST_NEKOGO, testStrMap, testNumMap, this)).thenReturn(
            null
        )

        UserActionUsecase(mockMetricsRepository).tap(UserAction.POST_NEKOGO, testStrMap, testNumMap, this)
        delay(20) // これがないと、内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間
        verify(mockMetricsRepository, times(1)).logEvent(
            AnalyticsEvent.TAP_POST_NEKOGO,
            testStrMap,
            testNumMap,
            this
        )
    }

    @Test
    fun complete_metricsRepositoryのlogEventが呼ばれること() = runBlocking {
        val testStrMap = mapOf("testStr" to "testStrval")
        val testNumMap = mapOf("testInt" to 4)
        `when`(
            mockMetricsRepository.logEvent(
                AnalyticsEvent.COMPLETE_SIGN_OUT,
                testStrMap,
                testNumMap,
                this
            )
        ).thenReturn(null)

        UserActionUsecase(mockMetricsRepository).complete(UserAction.SIGN_OUT, testStrMap, testNumMap, this)
        delay(20) // これがないと、内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間
        verify(mockMetricsRepository, times(1)).logEvent(
            AnalyticsEvent.COMPLETE_SIGN_OUT,
            testStrMap,
            testNumMap,
            this
        )
    }

    @Test
    fun complete_渡す行動種別に応じた値でlogEventが呼ばれること() = runBlocking {
        val testStrMap = mapOf("testStr" to "testStrval")
        val testNumMap = mapOf("testInt" to 4)
        `when`(
            mockMetricsRepository.logEvent(
                AnalyticsEvent.COMPLETE_POST_NEKOGO,
                testStrMap,
                testNumMap,
                this
            )
        ).thenReturn(null)

        UserActionUsecase(mockMetricsRepository).complete(UserAction.POST_NEKOGO, testStrMap, testNumMap, this)
        delay(20) // これがないと、内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間
        verify(mockMetricsRepository, times(1)).logEvent(
            AnalyticsEvent.COMPLETE_POST_NEKOGO,
            testStrMap,
            testNumMap,
            this
        )
    }
}
