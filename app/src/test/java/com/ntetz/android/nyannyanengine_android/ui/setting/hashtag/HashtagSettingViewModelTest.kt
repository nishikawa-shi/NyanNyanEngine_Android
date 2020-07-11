package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.TestUtil
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.usecase.IHashtagUsecase
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
class HashtagSettingViewModelTest {
    @Mock
    private lateinit var mockHashtagUsecase: IHashtagUsecase

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
        `when`(mockHashtagUsecase.getDefaultHashtags(TestUtil.any())).thenReturn(listOf())
        HashtagSettingViewModel(mockHashtagUsecase).initialize()
        delay(10) // これがないとCIでコケる

        verify(mockHashtagUsecase, times(1)).getDefaultHashtags(TestUtil.any())
        return@runBlocking
    }

    @Test
    fun initialize_対応するはっしゅたぐのliveDataが更新されること() = runBlocking {
        `when`(mockHashtagUsecase.getDefaultHashtags(TestUtil.any())).thenReturn(
            listOf(
                DefaultHashTagComponent(
                    9999,
                    "#testHashtaaaagVM",
                    true
                )
            )
        )

        val testViewModel = HashtagSettingViewModel(mockHashtagUsecase)
        testViewModel.initialize()
        delay(10) // イベント反映までの待ち時間

        Truth.assertThat(testViewModel.defaultHashtagComponents.value).isEqualTo(
            listOf(
                DefaultHashTagComponent(
                    9999,
                    "#testHashtaaaagVM",
                    true
                )
            )
        )
    }
}
