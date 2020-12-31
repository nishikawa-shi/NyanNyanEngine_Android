package com.ntetz.android.nyannyanengine_android.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
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
        delay(10) // これがないとCIでコケる
        MainViewModel(mockAccountUsecase, mockTweetUsecase).loadUserInfo()

        verify(mockAccountUsecase, times(1)).loadAccessToken(TestUtil.any())
        return@runBlocking
    }

    @Test
    fun loadUserInfo_対応するアクセストークン取得結果liveDataが更新されること() = runBlocking {
        `when`(mockAccountUsecase.loadAccessToken(TestUtil.any())).thenReturn(
            TwitterUserRecord(
                "testId", "testToken", "testTokenSecret", "testScName", "testName", null
            )
        )

        val testViewModel = MainViewModel(mockAccountUsecase, mockTweetUsecase)
        testViewModel.loadUserInfo()
        delay(10) // これがないとCIでコケる

        Truth.assertThat(testViewModel.userInfoEvent.value).isEqualTo(
            TwitterUserRecord(
                "testId", "testToken", "testTokenSecret", "testScName", "testName", null
            )
        )
        return@runBlocking
    }
}
