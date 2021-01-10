package com.ntetz.android.nyannyanengine_android.model.config

import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanUser
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord

object DefaultUserConfig {
    val notSignInUser = TwitterUserRecord(
        "28",
        oauthToken = "nyanToken",
        oauthTokenSecret = "nyanTokenSecret",
        screenName = "NNyansu",
        name = "にゃんにゃ先生",
        profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
    )

    val notSignInNyanNyanUser = NyanNyanUser(id = "28", nekosanPoint = 99999, tweetCount = 0)
}
