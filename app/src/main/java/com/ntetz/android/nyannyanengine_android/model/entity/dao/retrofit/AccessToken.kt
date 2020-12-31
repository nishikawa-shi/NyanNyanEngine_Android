package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

interface IAccessToken {
    val oauthToken: String
    val oauthTokenSecret: String
}

data class AccessToken(
    val isValid: Boolean,
    val errorDescription: String? = null,
    val id: String? = null,
    override val oauthToken: String = "",
    override val oauthTokenSecret: String = "",
    val screenName: String? = null
) : IAccessToken
