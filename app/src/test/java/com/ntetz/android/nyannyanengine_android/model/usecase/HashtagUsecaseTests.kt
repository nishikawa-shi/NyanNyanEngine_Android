package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import com.google.common.truth.Truth
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
    private lateinit var mockContext: Context

    @Test
    fun getDefaultHashtags_レポジトリ由来の値が得られること() = runBlocking {
        `when`(mockHashtagRepository.getDefaultHashtags(this, mockContext)).thenReturn(
            listOf(DefaultHashTagComponent(99999, "testHashtaaag", 30, true))
        )

        Truth.assertThat(
            HashtagUsecase(
                mockHashtagRepository
            ).getDefaultHashtags(this, mockContext)
        ).isEqualTo(listOf(DefaultHashTagComponent(99999, "testHashtaaag", 30, true)))
    }

    @Test
    fun updateDefaultHashtag_レポジトリのupdate用メソッドが1度呼ばれること() = runBlocking {
        val testDefaultHashTagComponent = DefaultHashTagComponent(3, "", 30, true)
        val expectedRecord = DefaultHashtagRecord(3, true)
        doNothing().`when`(mockHashtagRepository).updateDefaultHashtagRecord(expectedRecord, this)

        HashtagUsecase(mockHashtagRepository).updateDefaultHashtag(
            testDefaultHashTagComponent,
            this,
            mockContext
        )

        verify(mockHashtagRepository, times(1)).updateDefaultHashtagRecord(expectedRecord, this)
    }
}
