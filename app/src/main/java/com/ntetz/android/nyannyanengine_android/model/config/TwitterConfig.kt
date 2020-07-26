package com.ntetz.android.nyannyanengine_android.model.config

interface ITwitterConfig {
    fun getCallbackUrl(): String
    fun getConsumerKey(): String
    fun getApiSecret(): String
    fun getBaseEndpoint(): String
}

class TwitterConfig : ITwitterConfig {
    override fun getCallbackUrl(): String = "https://ntetz.com/authorized/"

    override fun getConsumerKey(): String = "abcde123GHI"

    override fun getApiSecret(): String = "jkl456MNO"

    override fun getBaseEndpoint(): String = "https://api.ntetz.com/"
}
