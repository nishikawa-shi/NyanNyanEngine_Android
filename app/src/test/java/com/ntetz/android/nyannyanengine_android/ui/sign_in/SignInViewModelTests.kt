package com.ntetz.android.nyannyanengine_android.ui.sign_in

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.SignInResultComponent
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignInViewModelTests {
    @Mock
    private lateinit var mockAccountUsecase: IAccountUsecase

    // この記述がないとviewModelScopeのlaunchがランタイムエラーする
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // この記述がないとviewModelScopeのlaunchがランタイムエラーする
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.IO)
    }

    // setMain(Dispatchers.IO)の対
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun executeSignIn_fetchAccessTokenが呼ばれること() = runBlocking {
        `when`(mockAccountUsecase.fetchAccessToken()).thenReturn(null)
        SignInViewModel(mockAccountUsecase).executeSignIn()
        delay(10) // これがないとCIでコケる

        verify(mockAccountUsecase, times(1)).fetchAccessToken()
        return@runBlocking
    }

    @Test
    fun executeSignIn_対応するアクセストークン取得結果liveDataが更新されること() = runBlocking {
        `when`(mockAccountUsecase.fetchAccessToken()).thenReturn(
            SignInResultComponent(
                isSucceeded = true,
                errorMessage = "testTokenEventTest"
            )
        )

        val testViewModel = SignInViewModel(mockAccountUsecase)
        testViewModel.executeSignIn()
        delay(10) // これがないとCIでコケる

        Truth.assertThat(testViewModel.signInEvent.value).isEqualTo(
            SignInResultComponent(
                isSucceeded = true,
                errorMessage = "testTokenEventTest"
            )
        )
        return@runBlocking
    }
}
