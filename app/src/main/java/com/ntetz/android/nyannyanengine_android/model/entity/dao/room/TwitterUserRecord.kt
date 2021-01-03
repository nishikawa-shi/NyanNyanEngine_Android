package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.IAccessToken
import java.security.MessageDigest

@Entity(tableName = "twitter_users")
data class TwitterUserRecord(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "oauth_token") override val oauthToken: String,
    @ColumnInfo(name = "oauth_token_secret") override val oauthTokenSecret: String,
    @ColumnInfo(name = "screen_name") val screenName: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "profile_image_url_https") val profileImageUrlHttps: String?
) : IAccessToken {
    val sealedUserId: String
        get() = id.toMD5()

    private fun String.toMD5(): String {
        val digest = MessageDigest.getInstance("MD5").also { it.update(this.toByteArray()) }
        val messageDigest = digest.digest()
        return messageDigest.fold(initial = StringBuilder()) { result, current ->
            var h = Integer.toHexString(0xFF and current.toInt())
            while (h.length < 2) h = "0$h"
            result.also { it.append(h) }
        }.toString()
    }
}
