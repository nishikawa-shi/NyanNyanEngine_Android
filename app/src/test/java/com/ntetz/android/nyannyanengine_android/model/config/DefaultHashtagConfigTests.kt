package com.ntetz.android.nyannyanengine_android.model.config

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DefaultHashtagConfigTests {
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
}
