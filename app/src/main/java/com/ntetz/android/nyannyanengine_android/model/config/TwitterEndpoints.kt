package com.ntetz.android.nyannyanengine_android.model.config

// Retrofit用インターフェースのアノテーション内でRepositoryと共通のpathの値を入れたく、また特に環境依存しないのでシングルトンにしている。
object TwitterEndpoints {
    const val baseEndpoint: String = "https://api.twitter.com/"
    const val authorizationHeaderName: String = "Authorization"

    const val authorizePagePath: String = "oauth/authorize"

    const val requestTokenPath: String = "oauth/request_token"
    const val accessTokenPath: String = "oauth/access_token"
    const val requestTokenMethod: String = "POST"
    const val accessTokenMethod: String = "POST"
    const val requestTokenOauthCallbackParamName: String = "oauth_callback"
}
