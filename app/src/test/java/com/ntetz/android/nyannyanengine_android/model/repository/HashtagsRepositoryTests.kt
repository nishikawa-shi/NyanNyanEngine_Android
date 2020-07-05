package com.ntetz.android.nyannyanengine_android.model.repository

import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HashtagsRepositoryTests {
    @Mock
    private lateinit var mockDefaultHashtagsDao: IDefaultHashtagsDao

    @Test
    fun allDefaultHashtags_daoのgetAll由来の値が取得できること() {
        `when`(mockDefaultHashtagsDao.allRecords()).thenReturn(
            MutableLiveData(listOf(DefaultHashtagRecord(9999, true)))
        )

        Truth.assertThat(HashtagsRepository(mockDefaultHashtagsDao).allDefaultHashtagRecords.value)
            .isEqualTo(listOf(DefaultHashtagRecord(9999, true)))
    }
}
