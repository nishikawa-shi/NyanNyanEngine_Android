package com.ntetz.android.nyannyanengine_android.ui.main

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
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
class MainViewModelTests {
    @Mock
    private lateinit var mockAccountUsecase: IAccountUsecase

    @Mock
    private lateinit var mockTweetUsecase: ITweetsUsecase

    @Mock
    private lateinit var mockUserActionUsecase: IUserActionUsecase

    @Mock
    private lateinit var mockContext: Context

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
    fun loadUserInfo_loadAccessTokenが呼ばれること() = runBlocking {
        `when`(mockAccountUsecase.loadAccessToken(TestUtil.any())).thenReturn(null)
        MainViewModel(mockAccountUsecase, mockTweetUsecase, mockUserActionUsecase, mockContext).loadUserInfo()
        delay(20) // これがないとCIでコケる

        verify(mockAccountUsecase, times(1)).loadAccessToken(TestUtil.any())
        return@runBlocking
    }

    @Test
    fun loadUserInfo_fetchNyanNyanConfigが呼ばれること() = runBlocking {
        `when`(mockAccountUsecase.fetchNyanNyanConfig()).thenReturn(null)
        MainViewModel(mockAccountUsecase, mockTweetUsecase, mockUserActionUsecase, mockContext).loadUserInfo()
        delay(20) // これがないとCIでコケる

        verify(mockAccountUsecase, times(1)).fetchNyanNyanConfig()
        return@runBlocking
    }

    @Test
    fun loadUserInfo_対応するアクセストークン取得結果liveDataが更新されること() = runBlocking {
        `when`(mockAccountUsecase.loadAccessToken(TestUtil.any())).thenReturn(
            TwitterUserRecord(
                "testId", "testToken", "testTokenSecret", "testScName", "testName", null
            )
        )

        val testViewModel = MainViewModel(mockAccountUsecase, mockTweetUsecase, mockUserActionUsecase, mockContext)
        testViewModel.loadUserInfo()
        delay(20) // これがないとCIでコケる

        Truth.assertThat(testViewModel.userInfoEvent.value).isEqualTo(
            TwitterUserRecord(
                "testId", "testToken", "testTokenSecret", "testScName", "testName", null
            )
        )
        return@runBlocking
    }

    @Test
    fun loadNyanNyanUserInfo_fetchNyanNyanUserが呼ばれること() = runBlocking {
        val mockUser = TwitterUserRecord(
            "mockTu",
            "testToken",
            "testSecret",
            "testScNm",
            "testName",
            null
        )
        `when`(mockAccountUsecase.loadAccessToken(TestUtil.any())).thenReturn(mockUser)
        `when`(mockAccountUsecase.fetchNyanNyanUser(mockUser)).thenReturn(null)
        MainViewModel(mockAccountUsecase, mockTweetUsecase, mockUserActionUsecase, mockContext).loadNyanNyanUserInfo()
        delay(20) // これがないとCIでコケる

        verify(mockAccountUsecase, times(1)).fetchNyanNyanUser(mockUser)
        return@runBlocking
    }

    @Test
    fun logOpenPostNekogoScreen_UserActionUsecaseのtapが呼ばれること() = runBlocking {
        `when`(
            mockUserActionUsecase.tap(TestUtil.any(), TestUtil.any(), TestUtil.any(), TestUtil.any())
        ).thenReturn(null)
        MainViewModel(
            mockAccountUsecase,
            mockTweetUsecase,
            mockUserActionUsecase,
            mockContext
        ).logOpenPostNekogoScreen()
        delay(20) // これがないとCIでコケる

        verify(mockUserActionUsecase, times(1)).tap(
            TestUtil.any(),
            TestUtil.any(),
            TestUtil.any(),
            TestUtil.any()
        )
    }

    @Test
    fun logToggleTweet_UserActionUsecaseのtapが呼ばれること() = runBlocking {
        `when`(
            mockUserActionUsecase.tap(TestUtil.any(), TestUtil.any(), TestUtil.any(), TestUtil.any())
        ).thenReturn(null)
        MainViewModel(mockAccountUsecase, mockTweetUsecase, mockUserActionUsecase, mockContext).logToggleTweet(3, true)

        delay(20) // これがないとCIでコケる
        verify(mockUserActionUsecase, times(1)).tap(
            TestUtil.any(),
            TestUtil.any(),
            TestUtil.any(),
            TestUtil.any()
        )
    }
}
