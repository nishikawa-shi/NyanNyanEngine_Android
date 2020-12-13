package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.util.Base64Encoder
import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

interface ITwitterSignature {
    fun getOAuthValue(user: TwitterUserRecord? = null): String
}

class TwitterSignature(
    private val requestMetadata: ITwitterRequestMetadata,
    private val twitterConfig: ITwitterConfig,
    private val base64Encoder: IBase64Encoder = Base64Encoder()
) : ITwitterSignature {
    override fun getOAuthValue(user: TwitterUserRecord?): String =
        listOf(
            "OAuth",
            getOAuthBody(
                tokenSecret = user?.oauthTokenSecret ?: "",
                token = user?.oauthToken ?: ""
            )
        ).joinToString(" ")

    fun getOAuthBody(tokenSecret: String, token: String): String {
        val oauthSignature = getBase64EncodedHmacSha1(
            sigKey = getCombinedSecretKeys(tokenSecret),
            sigData = getCombinedRequestMetadata(token)
        )
        return requestMetadata.getRequestParams(token = token)
            .plus(TwitterSignParam("oauth_signature", oauthSignature).toUrlString())
            .joinToString(",")
    }

    fun getBase64EncodedHmacSha1(sigKey: String, sigData: String): String {
        val algorithm = "HmacSHA1"
        val keySp = SecretKeySpec(sigKey.toByteArray(), algorithm)
        val mac = Mac.getInstance(algorithm).also { it.init(keySp) }
        return base64Encoder.encodeToStringForHttp(mac.doFinal(sigData.toByteArray()))
    }

    fun getCombinedSecretKeys(tokenSecret: String): String {
        return listOf(
            twitterConfig.apiSecret.addingPercentEncoding(),
            tokenSecret.addingPercentEncoding()
        ).joinToString("&")
    }

    fun getCombinedRequestMetadata(token: String): String {
        return listOf(
            requestMetadata.method,
            requestMetadata.fullUrl.addingPercentEncoding(),
            requestMetadata.getRequestParams(token = token).joinToString("&").addingPercentEncoding()
        ).joinToString("&")
    }

    private fun String.addingPercentEncoding(): String = URLEncoder.encode(this, "utf-8")
}
