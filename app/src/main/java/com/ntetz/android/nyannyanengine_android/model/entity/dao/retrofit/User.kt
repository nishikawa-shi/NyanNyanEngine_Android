package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.squareup.moshi.Json

data class User(
    val name: String,

    @Json(name = "screen_name")
    val screenName: String,

    @Json(name = "profile_image_url_https")
    var profileImageUrlHttps: String? = null
)
