package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.squareup.moshi.Json

data class AccessTokenInvalidation(
    @Json(name = "access_token")
    val accessToken: String
)
