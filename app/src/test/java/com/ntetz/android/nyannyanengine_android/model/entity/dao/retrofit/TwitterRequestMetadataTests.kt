package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TwitterRequestMetadataTests {
    @Mock
    private lateinit var mockTwitterConfig: ITwitterConfig

    private val mockOnetimeParams = TwitterOneTimeParams(oauthTimestamp = "1600000000", oauthNonce = "12345")

    @Test
    fun fullUrl_ドメインと第一階層のパスが結合された文字列が取得できること() {
        Truth.assertThat(
            TwitterRequestMetadata(
                method = "POST",
                path = "level1",
                oneTimeParams = mockOnetimeParams,
                twitterConfig = mockTwitterConfig
            ).fullUrl
        ).isEqualTo("https://api.twitter.com/level1")
    }

    @Test
    fun fullUrl_ドメインと複数階層のパスが結合された文字列が取得できること() {
        Truth.assertThat(
            TwitterRequestMetadata(
                method = "POST",
                path = "level1/level2",
                oneTimeParams = mockOnetimeParams,
                twitterConfig = mockTwitterConfig
            ).fullUrl
        ).isEqualTo("https://api.twitter.com/level1/level2")
    }

    @Test
    fun fullUrl_ドメインと特殊文字付きパスが変換されず結合されること() {
        Truth.assertThat(
            TwitterRequestMetadata(
                method = "POST",
                path = "level1/level2/level3?osakana=tabel&tabekata=yaku",
                oneTimeParams = mockOnetimeParams,
                twitterConfig = mockTwitterConfig
            ).fullUrl
        ).isEqualTo("https://api.twitter.com/level1/level2/level3?osakana=tabel&tabekata=yaku")
    }

    @Test
    fun requestParams_追加パラメータ未指定時基本パラメータのみ得られること() {
        `when`(mockTwitterConfig.consumerKey).thenReturn("abc123GHI")

        val testRequestMetadata = TwitterRequestMetadata(
            method = "POST",
            path = "level1/level2",
            oneTimeParams = mockOnetimeParams,
            twitterConfig = mockTwitterConfig
        )
        val exp = listOf(
            "oauth_consumer_key=abc123GHI",
            "oauth_nonce=12345",
            "oauth_signature_method=HMAC-SHA1",
            "oauth_timestamp=1600000000",
            "oauth_token=",
            "oauth_version=1.0"
        )
        Truth.assertThat(testRequestMetadata.getRequestParams(token = "")).isEqualTo(exp)
    }

    @Test
    fun requestParams_追加パラメータ指定時先頭に追加されること() {
        `when`(mockTwitterConfig.consumerKey).thenReturn("abc123GHI")

        val testResponseMetadata = TwitterRequestMetadata(
            additionalParams = listOf(TwitterSignParam("testKey1", "testVal1")),
            method = "POST",
            path = "level1/level2",
            oneTimeParams = mockOnetimeParams,
            twitterConfig = mockTwitterConfig
        )
        val exp = listOf(
            "testKey1=testVal1",
            "oauth_consumer_key=abc123GHI",
            "oauth_nonce=12345",
            "oauth_signature_method=HMAC-SHA1",
            "oauth_timestamp=1600000000",
            "oauth_token=",
            "oauth_version=1.0"
        )
        Truth.assertThat(testResponseMetadata.getRequestParams(token = "")).isEqualTo(exp)
    }

    @Test
    fun requestParams_追加パラメータ複数指定時順番が維持されること() {
        `when`(mockTwitterConfig.consumerKey).thenReturn("abc123GHI")

        val testResponseMetadata = TwitterRequestMetadata(
            additionalParams = listOf(
                TwitterSignParam("testKey1", "testVal1"),
                TwitterSignParam("testKey2", "testVal2")
            ),
            method = "POST",
            path = "level1/level2",
            oneTimeParams = mockOnetimeParams,
            twitterConfig = mockTwitterConfig
        )
        val exp = listOf(
            "testKey1=testVal1",
            "testKey2=testVal2",
            "oauth_consumer_key=abc123GHI",
            "oauth_nonce=12345",
            "oauth_signature_method=HMAC-SHA1",
            "oauth_timestamp=1600000000",
            "oauth_token=",
            "oauth_version=1.0"
        )
        Truth.assertThat(testResponseMetadata.getRequestParams(token = "")).isEqualTo(exp)
    }

    @Test
    fun requestParams_特殊文字つきパラメータがURLエンコードされること() {
        `when`(mockTwitterConfig.consumerKey).thenReturn("abc123GHI")

        val testResponseMetadata = TwitterRequestMetadata(
            additionalParams = listOf(
                TwitterSignParam("testKey/1", "testVal?1")
            ),
            method = "POST",
            path = "level1/level2",
            oneTimeParams = mockOnetimeParams,
            twitterConfig = mockTwitterConfig
        )
        Truth.assertThat(testResponseMetadata.getRequestParams(token = "")[0]).isEqualTo("testKey%2F1=testVal%3F1")
    }
}
