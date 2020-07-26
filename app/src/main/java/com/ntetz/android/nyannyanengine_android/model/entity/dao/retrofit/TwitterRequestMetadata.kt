package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints

interface ITwitterRequestMetadata {
    val additionalParams: List<TwitterSignParam>
    val method: String
    val path: String
    val fullUrl: String
    val oneTimeParams: TwitterOneTimeParams
    val requestParams: List<String>
}

data class TwitterRequestMetadata(
    override val additionalParams: List<TwitterSignParam> = listOf(),
    override val method: String,
    override val path: String,
    override val oneTimeParams: TwitterOneTimeParams = TwitterOneTimeParams(),
    private val twitterConfig: ITwitterConfig
) : ITwitterRequestMetadata {
    override val fullUrl: String
        get() = listOf(TwitterEndpoints.baseEndpoint, path).joinToString("")

    override val requestParams: List<String>
        get() {
            val baseParams = listOf(
                TwitterSignParam(
                    "oauth_consumer_key", twitterConfig.consumerKey
                ),
                TwitterSignParam(
                    "oauth_nonce", oneTimeParams.oauthNonce
                ),
                TwitterSignParam(
                    "oauth_signature_method", "HMAC-SHA1"
                ),
                TwitterSignParam(
                    "oauth_timestamp", oneTimeParams.oauthTimestamp
                ),
                TwitterSignParam(
                    "oauth_token", ""
                ),
                TwitterSignParam(
                    "oauth_version", "1.0"
                )
            )
            return additionalParams.plus(baseParams)
                .map { it.toUrlString() }
        }
}
