package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.util.Base64Encoder
import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

interface ITwitterSignature {
    fun getOAuthValue(): String
}

class TwitterSignature(
    private val requestMetadata: ITwitterRequestMetadata,
    private val twitterConfig: ITwitterConfig,
    private val base64Encoder: IBase64Encoder = Base64Encoder()
) : ITwitterSignature {
    override fun getOAuthValue(): String = listOf("OAuth", getOAuthBody()).joinToString(" ")

    fun getOAuthBody(): String {
        val oauthSignature = getBase64EncodedHmacSha1(
            sigKey = combinedSecretKeys,
            sigData = combinedRequestMetadata
        )
        return requestMetadata.requestParams
            .plus(TwitterSignParam("oauth_signature", oauthSignature).toUrlString())
            .joinToString(",")
    }

    fun getBase64EncodedHmacSha1(sigKey: String, sigData: String): String {
        val algorithm = "HmacSHA1"
        val keySp = SecretKeySpec(sigKey.toByteArray(), algorithm)
        val mac = Mac.getInstance(algorithm).also { it.init(keySp) }
        return base64Encoder.encodeToStringForHttp(mac.doFinal(sigData.toByteArray()))
    }

    val combinedSecretKeys: String
        get() = listOf(
            twitterConfig.getApiSecret().addingPercentEncoding(),
            ""
        ).joinToString("&")

    val combinedRequestMetadata: String
        get() = listOf(
            requestMetadata.method,
            requestMetadata.fullUrl.addingPercentEncoding(),
            requestMetadata.requestParams.joinToString("&").addingPercentEncoding()
        ).joinToString("&")

    private fun String.addingPercentEncoding(): String = URLEncoder.encode(this, "utf-8")
}
