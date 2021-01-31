package com.ntetz.android.nyannyanengine_android.model.repository

import android.content.Context
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import kotlinx.coroutines.delay
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
class HashtagsRepositoryTests {
    @Mock
    private lateinit var defaultHashtagConfig: IDefaultHashtagConfig

    @Mock
    private lateinit var mockDefaultHashtagsDao: IDefaultHashtagsDao

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun getDefaultHashtags_daoのgetAll由来の値が取得できること() = runBlocking {
        `when`(mockDefaultHashtagsDao.getAll()).thenReturn(
            listOf(DefaultHashtagRecord(9999, true))
        )
        `when`(defaultHashtagConfig.getTextBodyId(9999)).thenReturn(99919)
        `when`(mockContext.getString(99919)).thenReturn("testHashTaaag")

        Truth.assertThat(
            HashtagsRepository(defaultHashtagConfig, mockDefaultHashtagsDao).getDefaultHashtags(
                this,
                mockContext
            )
        ).isEqualTo(listOf(DefaultHashTagComponent(9999, "testHashTaaag", 0, true)))
    }

    @Test
    fun updateDefaultHashtagRecord_defaultHashtagsDaoのupdateOneが1度実行されること() = runBlocking {
        val testDefaultHashtagRecord = DefaultHashtagRecord(9999, true)
        doNothing().`when`(mockDefaultHashtagsDao).updateOne(testDefaultHashtagRecord)

        HashtagsRepository(defaultHashtagConfig, mockDefaultHashtagsDao).updateDefaultHashtagRecord(
            testDefaultHashtagRecord,
            this
        )
        delay(20) // これがないと、initialize内部のCoroutineの起動を見届けられない模様。CI上だと落ちるので長めの時間

        verify(mockDefaultHashtagsDao, times(1)).updateOne(testDefaultHashtagRecord)
    }
}
