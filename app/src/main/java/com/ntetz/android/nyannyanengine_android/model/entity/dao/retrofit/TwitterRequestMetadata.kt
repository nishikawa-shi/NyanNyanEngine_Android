package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints

interface ITwitterRequestMetadata {
    val additionalParams: List<TwitterSignParam>
    val method: String
    val path: String
    val appendAdditionalParamsToHead: Boolean
    val fullUrl: String
    val oneTimeParams: TwitterOneTimeParams
    fun getRequestParams(token: String = ""): List<String>
}

data class TwitterRequestMetadata(
    override val additionalParams: List<TwitterSignParam> = listOf(),
    override val method: String,
    override val path: String,
    override val appendAdditionalParamsToHead: Boolean = false, // リクエストトークンAPI等、一部APIではtrueにしないと署名に失敗して401エラーとなってしまう。
    override val oneTimeParams: TwitterOneTimeParams = TwitterOneTimeParams(),
    private val twitterConfig: ITwitterConfig
) : ITwitterRequestMetadata {
    override val fullUrl: String
        get() = listOf(TwitterEndpoints.baseEndpoint, path).joinToString("")

    // 保管先を、Roomではなくsuspend fun外でも動作させられる場所としたら、tokenはこのメソッド内で取得する方が良い。
    override fun getRequestParams(token: String): List<String> {
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
                "oauth_token", token
            ),
            TwitterSignParam(
                "oauth_version", "1.0"
            )
        )
        return if (appendAdditionalParamsToHead)
            (additionalParams.plus(baseParams).map { it.toUrlString() }) else (
                baseParams.plus(additionalParams).map { it.toUrlString() })
    }
}
