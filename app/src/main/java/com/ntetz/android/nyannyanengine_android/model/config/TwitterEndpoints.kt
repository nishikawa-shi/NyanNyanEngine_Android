package com.ntetz.android.nyannyanengine_android.model.config

// Retrofit用インターフェースのアノテーション内でRepositoryと共通のpathの値を入れたく、また特に環境依存しないのでシングルトンにしている。
object TwitterEndpoints {
    const val baseEndpoint: String = "https://api.twitter.com/"
    const val authorizationHeaderName: String = "Authorization"

    const val authorizePagePath: String = "oauth/authorize"
    const val requestTokenPath: String = "oauth/request_token"
    const val accessTokenPath: String = "oauth/access_token"
    const val verifyCredentialsPath: String = "1.1/account/verify_credentials.json"
    const val invalidateTokenPath: String = "1.1/oauth/invalidate_token"
    const val homeTimelinePath: String = "1.1/statuses/home_timeline.json"
    const val postTweetPath: String = "1.1/statuses/update.json"

    const val requestTokenMethod: String = "POST"
    const val accessTokenMethod: String = "POST"
    const val verifyCredentialsMethod: String = "GET"
    const val invalidateTokenMethod: String = "POST"
    const val homeTimelineMethod: String = "GET"
    const val postTweetMethod: String = "POST"

    const val requestTokenOauthCallbackParamName: String = "oauth_callback"
    const val accessTokenOauthVerifierParamName: String = "oauth_verifier"
    const val accessTokenOauthTokenParamName: String = "oauth_token"
    const val homeTimelineCountParamName: String = "count"
    const val homeTimelineMaxIdParamName: String = "max_id"
    const val postTweetStatusParamName: String = "status"

    const val homeTimelineCountParamDefaultValue: String = "200"
}
