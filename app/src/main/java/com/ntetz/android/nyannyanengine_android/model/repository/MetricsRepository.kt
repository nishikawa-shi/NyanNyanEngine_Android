package com.ntetz.android.nyannyanengine_android.model.repository

import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.AnalyticsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IMetricsRepository {
    suspend fun logEvent(
        type: AnalyticsEvent,
        textParams: Map<String, String>? = null,
        numParams: Map<String, Int>? = null,
        scope: CoroutineScope
    )
}

class MetricsRepository(
    private val firebaseClient: IFirebaseClient
) : IMetricsRepository {
    override suspend fun logEvent(
        type: AnalyticsEvent,
        textParams: Map<String, String>?,
        numParams: Map<String, Int>?,
        scope: CoroutineScope
    ) {
        scope.launch {
            withContext(Dispatchers.IO) {
                firebaseClient.logEvent(type, textParams, numParams)
            }
        }
    }
}
