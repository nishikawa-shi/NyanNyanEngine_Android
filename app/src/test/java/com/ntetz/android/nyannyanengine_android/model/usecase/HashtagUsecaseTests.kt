package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.repository.IHashtagsRepository
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HashtagUsecaseTests {
    @Mock
    private lateinit var mockHashtagRepository: IHashtagsRepository

    @Mock
    private lateinit var mockDefaultHashtagConfig: IDefaultHashtagConfig

    @Mock
    private lateinit var mockContext: Context

    // この記述がないとLiveDataのobserveがランタイムエラーする
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun defaultHashtags_observeしないとnullを返すこと() {
        `when`(mockHashtagRepository.allDefaultHashtagRecords).thenReturn(
            MutableLiveData(listOf(DefaultHashtagRecord(1, true)))
        )

        Truth.assertThat(
            HashtagUsecase(
                mockHashtagRepository,
                mockDefaultHashtagConfig,
                mockContext
            ).defaultHashtagComponents.value
        ).isNull()
    }

    @Test
    fun defaultHashtags_レポジトリにstringリソース有りidが登録されている時stringリソースが反映された値であること() {
        `when`(mockHashtagRepository.allDefaultHashtagRecords).thenReturn(
            MutableLiveData(listOf(DefaultHashtagRecord(99999, true)))
        )
        `when`(mockDefaultHashtagConfig.getTextBodyId(99999)).thenReturn(R.string.settings_title_hashtag_engine)
        `when`(mockContext.getString(R.string.settings_title_hashtag_engine)).thenReturn("#testHashtaaaag")

        HashtagUsecase(
            mockHashtagRepository,
            mockDefaultHashtagConfig,
            mockContext
        ).defaultHashtagComponents.observeForever {
            Truth.assertThat(it)
                .isEqualTo(listOf(DefaultHashTagComponent(99999, "#testHashtaaaag", true)))
        }
    }

    @Test
    fun defaultHashtags_レポジトリにstringリソース無しidが登録されている時空であること() {
        `when`(mockHashtagRepository.allDefaultHashtagRecords).thenReturn(
            MutableLiveData(listOf(DefaultHashtagRecord(99999, true)))
        )
        `when`(mockDefaultHashtagConfig.getTextBodyId(99999)).thenReturn(null)

        HashtagUsecase(
            mockHashtagRepository,
            mockDefaultHashtagConfig,
            mockContext
        ).defaultHashtagComponents.observeForever {
            Truth.assertThat(it)
                .isEqualTo(listOf<DefaultHashTagComponent>())
        }
    }
}
