package com.ntetz.android.nyannyanengine_android.model.config

import android.content.Context
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User

object DefaultTweetConfig {
    fun getNotSignInlist(context: Context) = listOf(
        Tweet(
            id = 28,
            createdAt = "Sat Jan 01 00:00:00 +0000 2021",
            text = context.getString(R.string.default_tweet_body),
            user = User(
                name = context.getString(R.string.default_twitter_name),
                screenName = context.getString(R.string.default_twitter_id),
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )

    fun getNoConnectionRequestList(context: Context) = listOf(
        Tweet(
            id = 28,
            createdAt = "Sat Jan 01 00:00:00 +0000 2021",
            text = context.getString(R.string.offline_tweet_body),
            user = User(
                name = context.getString(R.string.default_twitter_name),
                screenName = context.getString(R.string.default_twitter_id),
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )

    fun getTooManyRequestList(context: Context) = listOf(
        Tweet(
            id = 28,
            createdAt = "Sat Jan 01 00:00:00 +0000 2021",
            text = context.getString(R.string.too_many_request_body),
            user = User(
                name = context.getString(R.string.default_twitter_name),
                screenName = context.getString(R.string.default_twitter_id),
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )

    fun getUndefinedErrorList(context: Context) = listOf(
        Tweet(
            id = 28,
            createdAt = "Sat Jan 01 00:00:00 +0000 2021",
            text = context.getString(R.string.undefined_error_body),
            user = User(
                name = context.getString(R.string.default_twitter_name),
                screenName = context.getString(R.string.default_twitter_id),
                profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
            )
        )
    )
}
