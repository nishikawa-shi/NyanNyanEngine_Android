package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import java.util.*

data class TwitterOneTimeParams(
    val oauthTimestamp: String = (System.currentTimeMillis() / 1000L).toString(),
    val oauthNonce: String = Random().nextLong().toString()
)
