package com.ntetz.android.nyannyanengine_android.ui.post_nekogo

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
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
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PostNekogoViewModelTests {
    @Mock
    private lateinit var mockAccountUsecase: IAccountUsecase

    @Mock
    private lateinit var mockTweetsUsecase: ITweetsUsecase

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
        `when`(mockAccountUsecase.loadAccessToken(TestUtil.any(), TestUtil.any())).thenReturn(null)
        PostNekogoViewModel(mockAccountUsecase, mockTweetsUsecase, mockUserActionUsecase, mockContext).loadUserInfo()
        delay(20) // これがないとCIでコケる

        Mockito.verify(mockAccountUsecase, Mockito.times(1)).loadAccessToken(TestUtil.any(), TestUtil.any())
        return@runBlocking
    }

    @Test
    fun loadUserInfo_対応するアクセストークン取得結果liveDataが更新されること() = runBlocking {
        `when`(mockAccountUsecase.loadAccessToken(TestUtil.any(), TestUtil.any())).thenReturn(
            TwitterUserRecord(
                "testId", "testToken", "testTokenSecret", "testScName", "testName", null
            )
        )

        val testViewModel =
            PostNekogoViewModel(mockAccountUsecase, mockTweetsUsecase, mockUserActionUsecase, mockContext)
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
    fun postNekogo_postTweetが呼ばれること() = runBlocking {
        `when`(mockTweetsUsecase.postTweet(TestUtil.any(), TestUtil.any(), TestUtil.any())).thenReturn(null)
        PostNekogoViewModel(mockAccountUsecase, mockTweetsUsecase, mockUserActionUsecase, mockContext).postNekogo(
            "testNekogo",
            mockContext
        )
        delay(20) // これがないとCIでコケる

        Mockito.verify(mockTweetsUsecase, Mockito.times(1)).postTweet(TestUtil.any(), TestUtil.any(), TestUtil.any())
        return@runBlocking
    }

    @Test
    fun postNekogo_対応するツイート取得結果liveDataが更新されること() = runBlocking {
        `when`(mockTweetsUsecase.postTweet(TestUtil.any(), TestUtil.any(), TestUtil.any())).thenReturn(
            Tweet(
                id = 2828,
                text = "dummyUsCsNomPrev",
                createdAt = "3 gatsu 2 nichi",
                user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
            )
        )

        val testViewModel =
            PostNekogoViewModel(mockAccountUsecase, mockTweetsUsecase, mockUserActionUsecase, mockContext)
        testViewModel.postNekogo("dummyTTweeett", mockContext)
        delay(20) // これがないとCIでコケる

        Truth.assertThat(testViewModel.postTweetEvent.value).isEqualTo(
            Tweet(
                id = 2828,
                text = "dummyUsCsNomPrev",
                createdAt = "3 gatsu 2 nichi",
                user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
            )
        )
        return@runBlocking
    }

    @Test
    fun postNekogo_UserActionUsecaseのcompleteが呼ばれること() = runBlocking {
        `when`(mockTweetsUsecase.postTweet(TestUtil.any(), TestUtil.any(), TestUtil.any())).thenReturn(
            Tweet(
                id = 2828,
                text = "dummyUsCsNomPrev",
                createdAt = "3 gatsu 2 nichi",
                user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
            )
        )
        `when`(
            mockUserActionUsecase.complete(TestUtil.any(), TestUtil.any(), TestUtil.any(), TestUtil.any())
        ).thenReturn(null)
        PostNekogoViewModel(
            mockAccountUsecase,
            mockTweetsUsecase,
            mockUserActionUsecase,
            mockContext
        ).postNekogo("dummyInputText", mockContext)
        delay(20) // これがないとCIでコケる

        Mockito.verify(mockUserActionUsecase, Mockito.times(1)).complete(
            TestUtil.any(),
            TestUtil.any(),
            TestUtil.any(),
            TestUtil.any()
        )
    }
}
