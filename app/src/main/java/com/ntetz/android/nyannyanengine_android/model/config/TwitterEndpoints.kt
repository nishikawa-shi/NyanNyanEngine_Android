package com.ntetz.android.nyannyanengine_android.model.config

// Retrofit用インターフェースのアノテーション内でRepositoryと共通のpathの値を入れたく、また特に環境依存しないのでシングルトンにしている。
object TwitterEndpoints {
    const val requestTokenPath: String = "oauth/request_token"
    const val requestTokenMethod: String = "POST"
    const val requestTokenOauthCallbackParamName: String = "oauth_callback"
}
