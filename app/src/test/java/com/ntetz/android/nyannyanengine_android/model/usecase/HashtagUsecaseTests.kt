package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.repository.IHashtagsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HashtagUsecaseTests {
    @Mock
    private lateinit var mockHashtagRepository: IHashtagsRepository

    @Mock
    private lateinit var mockDefaultHashtagConfig: IDefaultHashtagConfig

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun getDefaultHashtagRecords_レポジトリにstringリソース有りidが登録されている時stringリソースが反映された値であること() = runBlocking {
        `when`(mockHashtagRepository.getDefaultHashtagRecords(this)).thenReturn(
            listOf(DefaultHashtagRecord(99999, true))
        )
        `when`(mockDefaultHashtagConfig.getTextBodyId(99999)).thenReturn(R.string.settings_title_hashtag_engine)
        `when`(mockContext.getString(R.string.settings_title_hashtag_engine)).thenReturn("#testHashtaaaag")

        Truth.assertThat(
            HashtagUsecase(
                mockHashtagRepository,
                mockDefaultHashtagConfig,
                mockContext
            ).getDefaultHashtags(this)
        )
            .isEqualTo(listOf(DefaultHashTagComponent(99999, "#testHashtaaaag", true)))
    }

    @Test
    fun getDefaultHashtagRecords_レポジトリにstringリソース無しidが登録されている時空であること() = runBlocking {
        `when`(mockHashtagRepository.getDefaultHashtagRecords(this)).thenReturn(
            listOf(DefaultHashtagRecord(99999, true))
        )
        `when`(mockDefaultHashtagConfig.getTextBodyId(99999)).thenReturn(null)

        Truth.assertThat(
            HashtagUsecase(
                mockHashtagRepository,
                mockDefaultHashtagConfig,
                mockContext
            ).getDefaultHashtags(this)
        )
            .isEqualTo(listOf<DefaultHashTagComponent>())
    }

    @Test
    fun updateDefaultHashtag_レポジトリのupdate用メソッドが1度呼ばれること() = runBlocking {
        val testDefaultHashTagComponent = DefaultHashTagComponent(3, "", true)
        val expectedRecord = DefaultHashtagRecord(3, true)
        doNothing().`when`(mockHashtagRepository).updateDefaultHashtagRecord(expectedRecord, this)

        HashtagUsecase(mockHashtagRepository, mockDefaultHashtagConfig, mockContext).updateDefaultHashtag(
            testDefaultHashTagComponent,
            this
        )

        verify(mockHashtagRepository, times(1)).updateDefaultHashtagRecord(expectedRecord, this)
    }
}
