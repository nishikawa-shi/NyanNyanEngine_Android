package com.ntetz.android.nyannyanengine_android.model.config

// Retrofit用インターフェースのアノテーション内でRepositoryと共通のpathの値を入れたく、また特に環境依存しないのでシングルトンにしている。
object TwitterEndpoints {
    const val baseEndpoint: String = "https://api.twitter.com/"
    const val authorizationHeaderName: String = "Authorization"

    const val authorizePagePath: String = "oauth/authorize"
    const val requestTokenPath: String = "oauth/request_token"
    const val accessTokenPath: String = "oauth/access_token"
    const val homeTimelinePath: String = "1.1/statuses/home_timeline.json"

    const val requestTokenMethod: String = "POST"
    const val accessTokenMethod: String = "POST"
    const val homeTimelineMethod: String = "GET"

    const val requestTokenOauthCallbackParamName: String = "oauth_callback"
    const val accessTokenOauthVerifierParamName: String = "oauth_verifier"
    const val accessTokenOauthTokenParamName: String = "oauth_token"
    const val homeTimelineCountParamName: String = "count"
    const val homeTimelineMaxIdParamName: String = "max_id"
    const val homeTimelineCountParamDefaultValue: String = "200"
}
