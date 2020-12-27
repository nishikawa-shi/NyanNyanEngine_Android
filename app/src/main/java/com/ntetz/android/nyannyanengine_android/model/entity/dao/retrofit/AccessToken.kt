package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

data class AccessToken(
    val isValid: Boolean,
    val errorDescription: String? = null,
    val userId: String? = null,
    val oauthToken: String? = null,
    val oauthTokenSecret: String? = null,
    val screenName: String? = null
)
