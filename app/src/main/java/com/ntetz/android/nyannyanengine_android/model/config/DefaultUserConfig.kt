package com.ntetz.android.nyannyanengine_android.model.config

import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanUser
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User

object DefaultUserConfig {
    val notSignInUser = User(
        name = "にゃんにゃ先生",
        screenName = "NNyansu",
        profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
    )

    val notSignInNyanNyanUser = NyanNyanUser(id = "28", nekosanPoint = 99999, tweetCount = 0)
}
