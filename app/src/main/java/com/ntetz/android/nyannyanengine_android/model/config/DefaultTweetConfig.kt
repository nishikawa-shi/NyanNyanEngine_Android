package com.ntetz.android.nyannyanengine_android.model.config

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User

object DefaultTweetConfig {
    fun getNotSignInlist(context: Context) = listOf(
        Tweet(
            id = 28,
            createdAt = "Sat Jan 01 00:00:00 +0000 2021",
            text = "左上のボタンからログインするにゃあ♪\nあなたのTwitterをネコ語化だにゃ",
            user = User(
                name = "にゃんにゃ先生",
                screenName = "NNyansu",
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )

    fun getNoConnectionRequestList(context: Context) = listOf(
        Tweet(
            id = 28,
            createdAt = "Sat Jan 01 00:00:00 +0000 2021",
            text = "にゃーん！回線状況を確認して、もう一回ひっぱり更新して欲しいにゃん😊",
            user = User(
                name = "にゃんにゃ先生",
                screenName = "NNyansu",
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )

    fun getTooManyRequestList(context: Context) = listOf(
        Tweet(
            id = 28,
            createdAt = "Sat Jan 01 00:00:00 +0000 2021",
            text = "いっぱい使ってくれてありがとにゃ🎊 \n猫さん休憩中だから、数十分後にもう一回試して欲しいにゃ",
            user = User(
                name = "にゃんにゃ先生",
                screenName = "NNyansu",
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )

    fun getUndefinedErrorList(context: Context) = listOf(
        Tweet(
            id = 28,
            createdAt = "Sat Jan 01 00:00:00 +0000 2021",
            text = "にゃーん。。。なんか調子が悪いにゃ。。。もうしばらくしてから遊んで欲しいにゃ。。。",
            user = User(
                name = "にゃんにゃ先生",
                screenName = "NNyansu",
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )
}
