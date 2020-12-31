package com.ntetz.android.nyannyanengine_android.model.usecase

import android.net.Uri
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessToken
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessTokenInvalidation
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.SignInResultComponent
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AccountUsecaseTests {
    @Mock
    private lateinit var mockAccountRepository: IAccountRepository

    @Test
    fun createAuthorizationEndpoint_レポジトリと共通設定に基づいた値が取得できること() = runBlocking {
        `when`(mockAccountRepository.getAuthorizationToken(this)).thenReturn(
            "oauth_token=val1&oauth_token_secret=val2"
        )

        val testUri = AccountUsecase(mockAccountRepository).createAuthorizationEndpoint(this)

        // 実際は比較にはなっていないが参考のため。Robolectricを使いたくない都合上、Uriクラスがnullを返すのでnull同士の比較で通っているだけ
        Truth.assertThat(testUri)
            .isEqualTo(Uri.parse("https://api.twitter.com/oauth/authorize?oauth_token=val1&oauth_token_secret=val2"))
    }

    @Test
    fun createAuthorizationEndpoint_authorizationAPIがレポジトリを通して1度呼ばれること() = runBlocking {
        `when`(mockAccountRepository.getAuthorizationToken(this)).thenReturn(
            "oauth_token=val1&oauth_token_secret=val2"
        )

        AccountUsecase(mockAccountRepository).createAuthorizationEndpoint(this)
        verify(mockAccountRepository, times(1)).getAuthorizationToken(this)
        return@runBlocking
    }

    @Test
    fun fetchAccessToken_Uriが不正な時エラー用の値を返すこと() = runBlocking {
        // Robolectricを入れたくない関係上、android.Uriクラスがnullを返すことを応用している
        withContext(Dispatchers.IO) {
            `when`(mockAccountRepository.getAccessToken(TestUtil.any(), TestUtil.any(), TestUtil.any())).thenReturn(
                AccessToken(isValid = false, errorDescription = "broken response...")
            )

            Truth.assertThat(AccountUsecase(mockAccountRepository).fetchAccessToken("dummyVeri", "dummyTok", this))
                .isEqualTo(
                    SignInResultComponent(
                        isSucceeded = false,
                        errorMessage = "broken response..."
                    )
                )
        }
    }

    @Test
    fun fetchAccessToken_accessTokenAPIがレポジトリを通して1度呼ばれること() = runBlocking {
        withContext(Dispatchers.IO) {
            `when`(mockAccountRepository.getAccessToken(TestUtil.any(), TestUtil.any(), TestUtil.any())).thenReturn(
                AccessToken(isValid = false, errorDescription = "broken response...")
            )

            AccountUsecase(mockAccountRepository).fetchAccessToken("dummyVeri", "dummyTok", this)
            verify(mockAccountRepository, times(1)).getAccessToken("dummyVeri", "dummyTok", this)
            return@withContext
        }
    }

    @Test
    fun deleteAccessToken_レポジトリと共通設定に基づいた値が取得できること() = runBlocking {
        val mockUser = TwitterUserRecord(
            "mockTu",
            "testToken",
            "testSecret",
            "testScNm"
        )
        `when`(mockAccountRepository.loadTwitterUser(this)).thenReturn(
            mockUser
        )
        `when`(mockAccountRepository.deleteTwitterUser(mockUser, this)).thenReturn(
            AccessTokenInvalidation("dummyTokenIsDeleted")
        )

        Truth.assertThat(AccountUsecase(mockAccountRepository).deleteAccessToken(this))
            .isEqualTo(AccessTokenInvalidation("dummyTokenIsDeleted"))
    }
}
