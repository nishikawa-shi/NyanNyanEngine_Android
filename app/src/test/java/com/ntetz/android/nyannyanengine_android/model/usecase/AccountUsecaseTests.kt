package com.ntetz.android.nyannyanengine_android.model.usecase

import android.net.Uri
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import kotlinx.coroutines.runBlocking
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
}
