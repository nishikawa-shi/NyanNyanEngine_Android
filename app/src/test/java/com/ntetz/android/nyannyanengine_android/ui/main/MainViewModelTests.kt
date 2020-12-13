package com.ntetz.android.nyannyanengine_android.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTests {
    @Mock
    private lateinit var mockTweetsUsecase: ITweetsUsecase

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
    fun initialize_loadAccessTokenが呼ばれること() = runBlocking {
        Mockito.`when`(mockTweetsUsecase.getTweets(TestUtil.any())).thenReturn(null)
        MainViewModel(mockTweetsUsecase).initialize()
        delay(10) // これがないとCIでコケる

        Mockito.verify(mockTweetsUsecase, Mockito.times(1))
            .getTweets(TestUtil.any())
        return@runBlocking
    }

    @Test
    fun initialize_対応するアクセストークン取得結果liveDataが更新されること() = runBlocking {
        Mockito.`when`(mockTweetsUsecase.getTweets(TestUtil.any())).thenReturn(
            listOf(
                Tweet(
                    id = 123,
                    text = "liveDataTest",
                    createdAt = "3 gatsu 2 nichi",
                    user = User(name = "nishik", screenName = "@nishik")
                )
            )
        )

        val testViewModel = MainViewModel(mockTweetsUsecase)
        testViewModel.initialize()
        delay(10) // これがないとCIでコケる

        Truth.assertThat(testViewModel.tweetsEvent.value).isEqualTo(
            listOf(
                Tweet(
                    id = 123,
                    text = "liveDataTest",
                    createdAt = "3 gatsu 2 nichi",
                    user = User(name = "nishik", screenName = "@nishik")
                )
            )
        )
        return@runBlocking
    }
}
