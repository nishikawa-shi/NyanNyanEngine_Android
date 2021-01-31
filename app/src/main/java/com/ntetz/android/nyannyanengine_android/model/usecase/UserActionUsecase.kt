package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.AnalyticsEvent
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.screen_transition.UserAction
import com.ntetz.android.nyannyanengine_android.model.repository.IMetricsRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

interface IUserActionUsecase {
    suspend fun tap(
        userAction: UserAction,
        textParams: Map<String, String>? = null,
        numParams: Map<String, Int>? = null,
        scope: CoroutineScope
    )

    suspend fun complete(
        userAction: UserAction,
        textParams: Map<String, String>? = null,
        numParams: Map<String, Int>? = null,
        scope: CoroutineScope
    )
}

class UserActionUsecase @Inject constructor(private val metricsRepository: IMetricsRepository) : IUserActionUsecase {
    override suspend fun tap(
        userAction: UserAction,
        textParams: Map<String, String>?,
        numParams: Map<String, Int>?,
        scope: CoroutineScope
    ) {
        val eventType = when (userAction) {
            UserAction.POST_NEKOGO -> AnalyticsEvent.TAP_POST_NEKOGO
            UserAction.SIGN_IN -> AnalyticsEvent.TAP_SIGN_IN
            UserAction.SIGN_OUT -> AnalyticsEvent.TAP_SIGN_OUT
            UserAction.SETTING_HASH_TAG -> AnalyticsEvent.TAP_SETTING_HASHTAG
            UserAction.TWEET -> AnalyticsEvent.TAP_TWEET
        }
        metricsRepository.logEvent(
            type = eventType,
            textParams = textParams,
            numParams = numParams,
            scope = scope
        )
    }

    override suspend fun complete(
        userAction: UserAction,
        textParams: Map<String, String>?,
        numParams: Map<String, Int>?,
        scope: CoroutineScope
    ) {
        val eventType = when (userAction) {
            UserAction.POST_NEKOGO -> AnalyticsEvent.COMPLETE_POST_NEKOGO
            UserAction.SIGN_IN -> AnalyticsEvent.COMPLETE_SIGN_IN
            UserAction.SIGN_OUT -> AnalyticsEvent.COMPLETE_SIGN_OUT
            UserAction.SETTING_HASH_TAG -> AnalyticsEvent.COMPLETE_SETTING_HASHTAG
            UserAction.TWEET -> return
        }
        metricsRepository.logEvent(
            type = eventType,
            textParams = textParams,
            numParams = numParams,
            scope = scope
        )
    }
}
