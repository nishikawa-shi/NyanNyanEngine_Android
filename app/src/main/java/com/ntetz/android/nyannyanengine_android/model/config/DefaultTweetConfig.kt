package com.ntetz.android.nyannyanengine_android.model.config

import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User

object DefaultTweetConfig {
    val notSignInlist = listOf(
        Tweet(
            id = 28,
            createdAt = "2828/2/8",
            text = "左上のボタンからログインするにゃあ♪\nあなたのTwitterをネコ語化だにゃ",
            user = User(
                name = "にゃんにゃ先生",
                screenName = "NNyansu",
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )

    val tooManyRequestList = listOf(
        Tweet(
            id = 28,
            createdAt = "2828/2/8",
            text = "いっぱい使ってくれてありがとにゃ🎊 \n猫さん休憩中だから、数十分後にもう一回試して欲しいにゃ",
            user = User(
                name = "にゃんにゃ先生",
                screenName = "NNyansu",
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )

    val undefinedErrorList = listOf(
        Tweet(
            id = 28,
            createdAt = "2828/2/8",
            text = "にゃーん。。。なんか調子が悪いにゃ。。。もうしばらくしてから遊んで欲しいにゃ。。。",
            user = User(
                name = "にゃんにゃ先生",
                screenName = "NNyansu",
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )
}
