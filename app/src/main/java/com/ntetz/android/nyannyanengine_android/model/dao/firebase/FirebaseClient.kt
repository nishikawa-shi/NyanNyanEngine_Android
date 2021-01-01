package com.ntetz.android.nyannyanengine_android.model.dao.firebase

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.AnalyticsEvent

interface IFirebaseClient {
    fun initialize()
    fun logEvent(type: AnalyticsEvent, textParams: Map<String, String>?, numParams: Map<String, Int>?)
}

class FirebaseClient : IFirebaseClient {
    private val analytics = Firebase.analytics
    private val auth = Firebase.auth

    override fun initialize() {
        auth.signInAnonymously()
    }

    override fun logEvent(type: AnalyticsEvent, textParams: Map<String, String>?, numParams: Map<String, Int>?) {
        analytics.logEvent(type.title) {
            textParams?.forEach { param(it.key, it.value) }
            numParams?.forEach { param(it.key, it.value.toLong()) }
        }
    }
}
