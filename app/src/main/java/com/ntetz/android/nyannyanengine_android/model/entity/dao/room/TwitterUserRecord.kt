package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.IAccessToken

@Entity(tableName = "twitter_users")
data class TwitterUserRecord(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "oauth_token") override val oauthToken: String,
    @ColumnInfo(name = "oauth_token_secret") override val oauthTokenSecret: String,
    @ColumnInfo(name = "screen_name") val screenName: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "profile_image_url_https") val profileImageUrlHttps: String?
) : IAccessToken
