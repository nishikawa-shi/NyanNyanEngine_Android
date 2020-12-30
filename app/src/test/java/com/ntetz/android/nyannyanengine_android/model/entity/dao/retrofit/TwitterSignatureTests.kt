package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.config.ITwitterConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TwitterSignatureTests {
    @Mock
    private lateinit var mockTwitterConfig: ITwitterConfig

    @Mock
    private lateinit var mockTwitterRequestMetadata: ITwitterRequestMetadata

    private val mockOnetimeParams = TwitterOneTimeParams(oauthTimestamp = "1600000000", oauthNonce = "12345")

    @Test
    fun getOAuthValue_リクエスト情報を元に値を生成できること() {
        `when`(mockTwitterConfig.consumerKey).thenReturn("abcConsumerKey")
        `when`(mockTwitterConfig.apiSecret).thenReturn("abcApiSecret")

        val testRequestMetadata = TwitterRequestMetadata(
            additionalParams = listOf(
                TwitterSignParam(
                    "oauth_callback",
                    "https://test.ntetz.com"
                )
            ),
            method = "POST",
            path = "oauth/request_token",
            appendAdditionalParamsToHead = true,
            oneTimeParams = mockOnetimeParams,
            twitterConfig = mockTwitterConfig
        )
        val testSignature = TwitterSignature(
            requestMetadata = testRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )

        Truth.assertThat(testSignature.getOAuthValue())
            .isEqualTo(
                listOf(
                    "OAuth oauth_callback=https%3A%2F%2Ftest.ntetz.com",
                    "oauth_consumer_key=abcConsumerKey",
                    "oauth_nonce=12345",
                    "oauth_signature_method=HMAC-SHA1",
                    "oauth_timestamp=1600000000",
                    "oauth_token=",
                    "oauth_version=1.0",
                    "oauth_signature=OOuKWsgJkf9byY4o9xEjyu1AyeI%3D"
                ).joinToString(",")
            )
    }

    @Test
    fun getOAuthValue_先頭にOAuthとついたURLエンコード済署名つきパラメータ一覧が得られること() {
        `when`(mockTwitterRequestMetadata.getRequestParams("")).thenReturn(listOf("key1=value1"))
        `when`(mockTwitterRequestMetadata.method).thenReturn("")
        `when`(mockTwitterRequestMetadata.fullUrl).thenReturn("")
        `when`(mockTwitterConfig.apiSecret).thenReturn("")

        val testSignature = TwitterSignature(
            requestMetadata = mockTwitterRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )
        Truth.assertThat(testSignature.getOAuthValue())
            .isEqualTo("OAuth key1=value1,oauth_signature=9bkADMZLChr-L2qV0XkuYLMqHtY%3D")
    }

    @Test
    fun getOAuthValue_ログイン時のユーザー情報が反映された値が得られること() {
        `when`(mockTwitterRequestMetadata.getRequestParams("oauthVTestToken")).thenReturn(listOf("key1=value1"))
        `when`(mockTwitterRequestMetadata.method).thenReturn("")
        `when`(mockTwitterRequestMetadata.fullUrl).thenReturn("")
        `when`(mockTwitterConfig.apiSecret).thenReturn("")

        val testSignature = TwitterSignature(
            requestMetadata = mockTwitterRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )
        val testUser = TwitterUserRecord(
            id = "oauthVTestId",
            oauthToken = "oauthVTestToken",
            oauthTokenSecret = "oauthVTestSecret",
            screenName = "oauthVTestSNm"
        )
        Truth.assertThat(testSignature.getOAuthValue(testUser))
            .isEqualTo("OAuth key1=value1,oauth_signature=BJw5r3yoEMMGBIjV5VCKXgOh4uc%3D")
    }

    @Test
    fun getOAuthBody_URLエンコード署名つきパラメータ一覧が得られること() {
        `when`(mockTwitterRequestMetadata.getRequestParams()).thenReturn(listOf("key1=value1"))
        `when`(mockTwitterRequestMetadata.method).thenReturn("")
        `when`(mockTwitterRequestMetadata.fullUrl).thenReturn("")
        `when`(mockTwitterConfig.apiSecret).thenReturn("")

        val testSignature = TwitterSignature(
            requestMetadata = mockTwitterRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )
        Truth.assertThat(testSignature.getOAuthBody(token = "", tokenSecret = ""))
            .isEqualTo("key1=value1,oauth_signature=9bkADMZLChr-L2qV0XkuYLMqHtY%3D")
    }

    @Test
    fun getBase64EncodedHmacSha1_与えた秘密鍵とデータに対応する署名が得られること() {
        val testSignature = TwitterSignature(
            requestMetadata = mockTwitterRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )
        Truth.assertThat(testSignature.getBase64EncodedHmacSha1("abcKey", "defData"))
            .isEqualTo("TgNZ5JvKg7L6xk88nxXpELLKW7s=")
    }

    @Test
    fun combinedSecretKeys_秘密鍵がアンパサンドで結合された文字列が得られること() {
        `when`(mockTwitterConfig.apiSecret).thenReturn("abcApiSecret")

        val testSignature = TwitterSignature(
            requestMetadata = mockTwitterRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )
        Truth.assertThat(testSignature.getCombinedSecretKeys(tokenSecret = "")).isEqualTo("abcApiSecret&")
    }

    @Test
    fun combinedSecretKeys_特殊文字がURLエンコードされた秘密鍵を含むこと() {
        `when`(mockTwitterConfig.apiSecret).thenReturn("abc/ApiSecret")

        val testSignature = TwitterSignature(
            requestMetadata = mockTwitterRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )
        Truth.assertThat(testSignature.getCombinedSecretKeys(tokenSecret = ""))
            .isEqualTo("abc%2FApiSecret&")
    }

    @Test
    fun combinedRequestMetadata_リクエスト情報がアンパサンドで結合された文字列が得られること() {
        `when`(mockTwitterRequestMetadata.method).thenReturn("POST")
        `when`(mockTwitterRequestMetadata.fullUrl).thenReturn("testFullUrl")
        `when`(mockTwitterRequestMetadata.getRequestParams()).thenReturn(listOf("param1"))

        val testSignature = TwitterSignature(
            requestMetadata = mockTwitterRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )
        Truth.assertThat(testSignature.getCombinedRequestMetadata(token = ""))
            .isEqualTo("POST&testFullUrl&param1")
    }

    @Test
    fun combinedRequestMetadata_リクエストパラメータが複数ある時アンバサンドで結合後URLエンコードされること() {
        `when`(mockTwitterRequestMetadata.method).thenReturn("POST")
        `when`(mockTwitterRequestMetadata.fullUrl).thenReturn("testFullUrl")
        `when`(mockTwitterRequestMetadata.getRequestParams()).thenReturn(listOf("param1", "param2"))

        val testSignature = TwitterSignature(
            requestMetadata = mockTwitterRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )
        Truth.assertThat(testSignature.getCombinedRequestMetadata(token = ""))
            .isEqualTo("POST&testFullUrl&param1%26param2")
    }

    @Test
    fun combinedRequestMetadata_リクエスト情報内の特殊文字がURLエンコードされること() {
        `when`(mockTwitterRequestMetadata.method).thenReturn("POST")
        `when`(mockTwitterRequestMetadata.fullUrl).thenReturn("https://test.ntetz.com")
        `when`(mockTwitterRequestMetadata.getRequestParams()).thenReturn(listOf("key1=value1"))

        val testSignature = TwitterSignature(
            requestMetadata = mockTwitterRequestMetadata,
            twitterConfig = mockTwitterConfig,
            base64Encoder = TestUtil.mockBase64Encoder
        )
        Truth.assertThat(testSignature.getCombinedRequestMetadata(token = ""))
            .isEqualTo("POST&https%3A%2F%2Ftest.ntetz.com&key1%3Dvalue1")
    }
}
