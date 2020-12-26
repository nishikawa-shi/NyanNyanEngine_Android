package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_tweets")
data class CachedTweetRecord(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "user_screen_name") val userScreenName: String,
    @ColumnInfo(name = "profile_image_url_https") val profileImageUrlHttps: String
)
