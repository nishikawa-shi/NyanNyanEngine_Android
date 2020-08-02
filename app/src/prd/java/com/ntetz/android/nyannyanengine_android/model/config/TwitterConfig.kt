package com.ntetz.android.nyannyanengine_android.model.config

class TwitterConfig : ITwitterConfig {
    override val callbackUrl: String = "https://nyannyanengine.firebaseapp.com/authorized/"
    override val apiSecret: String
        get() = getApiSecretFromJniPrd()
    override val consumerKey: String
        get() = getConsumerKeyFromJniPrd()

    private external fun getApiSecretFromJniPrd(): String
    private external fun getConsumerKeyFromJniPrd(): String

    companion object {
        init {
            System.loadLibrary("secure-config-master-lib")
        }
    }
}
