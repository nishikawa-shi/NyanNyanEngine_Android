package com.ntetz.android.nyannyanengine_android.model.config

class TwitterConfig : ITwitterConfig {
    override val callbackUrl: String = "https://nyannyanengine-ios-d.firebaseapp.com/authorized/"
    override val apiSecret: String
        get() = getApiSecretFromJniDev()
    override val consumerKey: String
        get() = getConsumerKeyFromJniDev()

    private external fun getApiSecretFromJniDev(): String
    private external fun getConsumerKeyFromJniDev(): String

    companion object {
        init {
            System.loadLibrary("secure-config-master-lib")
        }
    }
}
