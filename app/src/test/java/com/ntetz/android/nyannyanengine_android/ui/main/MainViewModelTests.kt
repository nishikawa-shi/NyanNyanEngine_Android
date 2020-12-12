package com.ntetz.android.nyannyanengine_android.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTests {
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
    fun initialize_loadAccessTokenが呼ばれること() = runBlocking {
        Mockito.`when`(mockAccountUsecase.loadAccessToken(TestUtil.any())).thenReturn(null)
        MainViewModel(mockAccountUsecase).initialize()
        delay(10) // これがないとCIでコケる

        Mockito.verify(mockAccountUsecase, Mockito.times(1))
            .loadAccessToken(TestUtil.any())
        return@runBlocking
    }

    @Test
    fun initialize_対応するアクセストークン取得結果liveDataが更新されること() = runBlocking {
        Mockito.`when`(mockAccountUsecase.loadAccessToken(TestUtil.any())).thenReturn(
            TwitterUserRecord(
                id = "dummyId",
                oauthToken = "dummyTkn",
                oauthTokenSecret = "dummyScrt",
                screenName = "dummyScName"
            )
        )

        val testViewModel = MainViewModel(mockAccountUsecase)
        testViewModel.initialize()
        delay(10) // これがないとCIでコケる

        Truth.assertThat(testViewModel.twitterUserEvent.value).isEqualTo(
            TwitterUserRecord(
                id = "dummyId",
                oauthToken = "dummyTkn",
                oauthTokenSecret = "dummyScrt",
                screenName = "dummyScName"
            )
        )
        return@runBlocking
    }
}
