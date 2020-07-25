package com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TwitterSignParamTests {
    @Test
    fun toUrlString_特殊文字を含まないパラメータの値が変換されないこと() {
        val testTwitterSignParam = TwitterSignParam("name1", "value1")
        Truth.assertThat(testTwitterSignParam.toUrlString()).isEqualTo("name1=value1")
    }

    @Test
    fun toUrlString_特殊文字を含むパラメータの値が変換されること() {
        val testTwitterSignParam = TwitterSignParam("name1", "value1=")
        Truth.assertThat(testTwitterSignParam.toUrlString()).isEqualTo("name1=value1%3D")
    }
}
