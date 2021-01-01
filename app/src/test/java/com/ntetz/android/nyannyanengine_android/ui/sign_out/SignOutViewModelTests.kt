package com.ntetz.android.nyannyanengine_android.ui.sign_out

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessTokenInvalidation
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
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
class SignOutViewModelTests {
    @Mock
    private lateinit var mockAccountUsecase: IAccountUsecase

    @Mock
    private lateinit var mockUserActionUsecase: IUserActionUsecase

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
    fun executeSignOut_deleteAccessTokenが呼ばれること() = runBlocking {
        `when`(mockAccountUsecase.deleteAccessToken(TestUtil.any())).thenReturn(null)
        SignOutViewModel(mockAccountUsecase, mockUserActionUsecase).executeSignOut()
        delay(10) // これがないとCIでコケる

        verify(mockAccountUsecase, times(1)).deleteAccessToken(TestUtil.any())
        return@runBlocking
    }

    @Test
    fun executeSignOut_対応するアクセストークン取得結果liveDataが更新されること() = runBlocking {
        `when`(mockAccountUsecase.deleteAccessToken(TestUtil.any())).thenReturn(
            AccessTokenInvalidation("viewModelTestInvalidation!")
        )

        val testViewModel = SignOutViewModel(mockAccountUsecase, mockUserActionUsecase)
        testViewModel.executeSignOut()
        delay(10) // これがないとCIでコケる

        Truth.assertThat(testViewModel.signOutEvent.value).isEqualTo(
            AccessTokenInvalidation("viewModelTestInvalidation!")
        )
        return@runBlocking
    }

    @Test
    fun executeSignOut_UserActionUsecaseのcompleteが呼ばれること() = runBlocking {
        `when`(mockAccountUsecase.deleteAccessToken(TestUtil.any())).thenReturn(
            AccessTokenInvalidation("viewModelTestInvalidation!")
        )
        `when`(
            mockUserActionUsecase.complete(TestUtil.any(), TestUtil.any(), TestUtil.any(), TestUtil.any())
        ).thenReturn(null)
        SignOutViewModel(mockAccountUsecase, mockUserActionUsecase).executeSignOut()
        delay(50) // これがないとCIでコケる

        verify(mockUserActionUsecase, times(1)).complete(
            TestUtil.any(),
            TestUtil.any(),
            TestUtil.any(),
            TestUtil.any()
        )
    }
}
