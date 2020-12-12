package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "twitter_users")
data class TwitterUserRecord(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "oauth_token") val oauthToken: String,
    @ColumnInfo(name = "oauth_token_secret") val oauthTokenSecret: String,
    @ColumnInfo(name = "screen_name") val screenName: String
)
