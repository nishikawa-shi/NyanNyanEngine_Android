package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import java.net.URLEncoder

data class TwitterSignParam(
    val name: String,
    val value: String
) {
    fun toUrlString(): String {
        return listOf(
            URLEncoder.encode(name, "utf-8"),
            URLEncoder.encode(value, "utf-8")
        ).joinToString("=")
    }
}
