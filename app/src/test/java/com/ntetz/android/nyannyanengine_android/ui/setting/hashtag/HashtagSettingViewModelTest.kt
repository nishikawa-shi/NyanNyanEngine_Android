package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.usecase.IHashtagUsecase
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
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HashtagSettingViewModelTest {
    @Mock
    private lateinit var mockHashtagUsecase: IHashtagUsecase

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
    fun initialize_getDefaultHashtagsが呼ばれること() = runBlocking {
        `when`(mockHashtagUsecase.getDefaultHashtags(TestUtil.any(), TestUtil.any())).thenReturn(listOf())
        HashtagSettingViewModel(mockHashtagUsecase, mockUserActionUsecase, mockContext).initialize()
        delay(20) // これがないとCIでコケる

        verify(mockHashtagUsecase, times(1)).getDefaultHashtags(TestUtil.any(), TestUtil.any())
        return@runBlocking
    }

    @Test
    fun initialize_対応するはっしゅたぐのliveDataが更新されること() = runBlocking {
        `when`(mockHashtagUsecase.getDefaultHashtags(TestUtil.any(), TestUtil.any())).thenReturn(
            listOf(
                DefaultHashTagComponent(
                    9999,
                    "#testHashtaaaagVM",
                    30,
                    true
                )
            )
        )

        val testViewModel = HashtagSettingViewModel(mockHashtagUsecase, mockUserActionUsecase, mockContext)
        testViewModel.initialize()
        delay(20) // イベント反映までの待ち時間

        Truth.assertThat(testViewModel.defaultHashtagComponents.value).isEqualTo(
            listOf(
                DefaultHashTagComponent(
                    9999,
                    "#testHashtaaaagVM",
                    30,
                    true
                )
            )
        )
    }

    @Test
    fun updateDefaultHashtagComponent_updateDefaultHashtagが呼ばれること() = runBlocking {
        val mockTag = DefaultHashTagComponent(5, "testtaaag", 30, true)
        doNothing().`when`(mockHashtagUsecase).updateDefaultHashtag(TestUtil.any(), TestUtil.any(), TestUtil.any())

        HashtagSettingViewModel(
            mockHashtagUsecase,
            mockUserActionUsecase,
            mockContext
        ).updateDefaultHashtagComponent(
            mockTag
        )
        delay(20) // これがないと、内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間
        verify(mockHashtagUsecase, times(1)).updateDefaultHashtag(TestUtil.any(), TestUtil.any(), TestUtil.any())
    }

    @Test
    fun updateDefaultHashtagComponent_userActionUsecaseのcompleteが呼ばれること() = runBlocking {
        val mockTag = DefaultHashTagComponent(5, "testtaaag", 30, true)
        doNothing().`when`(mockHashtagUsecase).updateDefaultHashtag(TestUtil.any(), TestUtil.any(), TestUtil.any())
        `when`(
            mockUserActionUsecase.complete(
                TestUtil.any(),
                TestUtil.any(),
                TestUtil.any(),
                TestUtil.any()
            )
        ).thenReturn(null)

        HashtagSettingViewModel(
            mockHashtagUsecase,
            mockUserActionUsecase,
            mockContext
        ).updateDefaultHashtagComponent(
            mockTag
        )
        delay(20) // これがないと、内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間
        verify(mockUserActionUsecase, times(1)).complete(TestUtil.any(), TestUtil.any(), TestUtil.any(), TestUtil.any())
    }
}
