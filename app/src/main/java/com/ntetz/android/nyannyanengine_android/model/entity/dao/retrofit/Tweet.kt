package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.squareup.moshi.Json

data class Tweet(
    val id: Long,

    val text: String,

    @Json(name = "created_at")
    val createdAt: String,

    val user: User
) {
    val isError: Boolean
        get() = id.toInt() == 28
}
