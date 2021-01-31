package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import android.content.Context
import com.ntetz.android.nyannyanengine_android.R
import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*

data class Tweet(
    val id: Long,

    val text: String,

    @Json(name = "created_at")
    val createdAt: String,

    val user: User
) {
    val isError: Boolean
        get() = id.toInt() == 28

    var point: Int = 0

    fun tweetedAt(context: Context): String? {
        val tweetDate = createdAt.toDate() ?: return null
        val now = Date()
        val diff = now.time - tweetDate.time
        if (diff < 1000 * 60) {
            return listOf((diff / (1000)).toString(), context.getString(R.string.time_suffix_sec)).joinToString("")
        }
        if (diff < 1000 * 60 * 60) {
            return listOf((diff / (1000 * 60)).toString(), context.getString(R.string.time_suffix_min)).joinToString("")
        }
        if (diff < 1000 * 60 * 60 * 24) {
            return listOf(
                (diff / (1000 * 60 * 60)).toString(),
                context.getString(R.string.time_suffix_hour)
            ).joinToString("")
        }
        return listOf(
            (diff / (1000 * 60 * 60 * 24)).toString(),
            context.getString(R.string.time_suffix_day)
        ).joinToString("")
    }

    private fun String.toDate(): Date? {
        return SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US).parse(this)
    }
}
