package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.squareup.moshi.Json

data class User(
    val name: String,

    @Json(name = "screen_name")
    val screenName: String,

    @Json(name = "profile_image_url_https")
    var profileImageUrlHttps: String? = null
) {
    val fineImageUrl: String?
        get() {
            val match =
                Regex("""^https?://(.+)_normal(.+)$""").find(
                    this.profileImageUrlHttps ?: return this.profileImageUrlHttps
                )?.groups
                    ?: return this.profileImageUrlHttps
            if (match.size != 3) {
                return this.profileImageUrlHttps
            }

            val urlBody = match[1]?.value ?: return this.profileImageUrlHttps
            val fileExt = match[2]?.value ?: return this.profileImageUrlHttps
            return listOf("https://", urlBody, fileExt).joinToString("")
        }
}
