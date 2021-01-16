package com.ntetz.android.nyannyanengine_android.model.config

import android.content.Context
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanUser
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord

object DefaultUserConfig {
    fun getNotSignInUser(context: Context) = TwitterUserRecord(
        "28",
        oauthToken = "nyanToken",
        oauthTokenSecret = "nyanTokenSecret",
        screenName = context.getString(R.string.default_twitter_id),
        name = context.getString(R.string.default_twitter_name),
        profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
    )

    fun getNotSignInNyanNyanUser() = NyanNyanUser(id = "28", nekosanPoint = 99999, tweetCount = 0)
}
