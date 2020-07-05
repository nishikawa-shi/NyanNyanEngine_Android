package com.ntetz.android.nyannyanengine_android.model.config

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DefaultHashtagConfigTests {
    @Mock
    private lateinit var mockDefaultHashtagsDao: IDefaultHashtagsDao

    @Test
    fun getInitializationRecords_組み込まれた設定由来の値が取得できること() {
        Truth.assertThat(DefaultHashtagConfig().getInitializationRecords()[0]).isEqualTo(DefaultHashtagRecord(1, true))
    }

    @Test
    fun getTextBodyId_組み込まれたidに対して組み込まれた設定由来の値が取得できること() {
        Truth.assertThat(DefaultHashtagConfig().getTextBodyId(1)).isEqualTo(R.string.settings_title_hashtag_engine)
    }

    @Test
    fun getTextBodyId_組み込まれていないidに対してnullが取得できること() {
        Truth.assertThat(DefaultHashtagConfig().getTextBodyId(-1)).isNull()
    }

    @Test
    fun populate_データベースが空の時登録処理が走ること() = runBlocking {
        val config = DefaultHashtagConfig()
        val firstEmbeddedRecord = config.getInitializationRecords()[0]
        `when`(mockDefaultHashtagsDao.getAll()).thenReturn(listOf())

        config.populate(mockDefaultHashtagsDao)
        verify(mockDefaultHashtagsDao, times(1)).insert(firstEmbeddedRecord)
    }

    @Test
    fun populate_データベースに組み込みidのレコードが存在しない時登録処理が走ること() = runBlocking {
        val config = DefaultHashtagConfig()
        val firstEmbeddedRecord = config.getInitializationRecords()[0]
        `when`(mockDefaultHashtagsDao.getAll()).thenReturn(listOf(DefaultHashtagRecord(-1, true)))

        config.populate(mockDefaultHashtagsDao)
        verify(mockDefaultHashtagsDao, times(1)).insert(firstEmbeddedRecord)
    }

    @Test
    fun populate_データベースに組み込みidのレコードが存在する時登録処理が走らないこと() = runBlocking {
        val config = DefaultHashtagConfig()
        val firstEmbeddedRecord = config.getInitializationRecords()[0]
        `when`(mockDefaultHashtagsDao.getAll()).thenReturn(listOf(DefaultHashtagRecord(firstEmbeddedRecord.id, true)))

        config.populate(mockDefaultHashtagsDao)
        verify(mockDefaultHashtagsDao, times(0)).insert(firstEmbeddedRecord)
    }
}
