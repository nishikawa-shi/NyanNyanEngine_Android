package com.ntetz.android.nyannyanengine_android.util

import android.content.Context
import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NekosanExtensionTests {
    @Mock
    private lateinit var mockContext: Context

    @Before
    fun setup() {
        `when`(mockContext.getString(R.string.nekosan_nakigoe_type1)).thenReturn("にゃ1")
        `when`(mockContext.getString(R.string.nekosan_nakigoe_type3)).thenReturn("にゃ3")
        `when`(mockContext.getString(R.string.nekosan_nakigoe_type5)).thenReturn("にゃ5")
        `when`(mockContext.getString(R.string.nekosan_nakigoe_type8)).thenReturn("にゃ8")
        `when`(mockContext.getString(R.string.settings_title_hashtag_nadenade)).thenReturn("#なでてほしいにゃ🧶")
        `when`(mockContext.getString(R.string.settings_title_hashtag_engine)).thenReturn("#にゃんにゃんエンジン")
    }

    @Test
    fun StringToNyanNyan_猫語化されることはそのまま猫語がtrue() {
        // '猫語テストだにゃ' はMD5にすると 5da423bf26a614a1d79f632fd89a7552 。その先頭3文字と末尾1文字が使われる
        Truth.assertThat("猫語テストだにゃ".toNyanNyan(mockContext)).isEqualTo("にゃ3にゃ8にゃ5🙁")
    }

    @Test
    fun StringToNyanNyan_猫語はそのまま猫語がtrue() {
        Truth.assertThat("にゃ8にゃ1にゃ1 #なでてほしいにゃ🧶".toNyanNyan(mockContext)).isEqualTo("にゃ8にゃ1にゃ1 #なでてほしいにゃ🧶")
    }

    @Test
    fun isNekogo_type1単体がtrue() {
        Truth.assertThat("にゃ1".isNekogo(mockContext)).isTrue()
    }

    @Test
    fun isNekogo_type8単体がtrue() {
        Truth.assertThat("にゃ8".isNekogo(mockContext)).isTrue()
    }

    @Test
    fun isNekogo_type1の3回繰り返しがtrue() {
        Truth.assertThat("にゃ1にゃ1にゃ1".isNekogo(mockContext)).isTrue()
    }

    @Test
    fun isNekogo_type1の4回繰り返しがfalse() {
        Truth.assertThat("にゃ1にゃ1にゃ1にゃ1".isNekogo(mockContext)).isFalse()
    }

    @Test
    fun isNekogo_type1単体と接尾辞絵文字1つがtrue() {
        Truth.assertThat("にゃ1😊".isNekogo(mockContext)).isTrue()
    }

    @Test
    fun isNekogo_type1単体と非接尾辞絵文字1つがfalse() {
        Truth.assertThat("にゃ1🥶".isNekogo(mockContext)).isFalse()
    }

    @Test
    fun isNekogo_type1単体とはっしゅたぐ1つがtrue() {
        Truth.assertThat("にゃ1 #なでてほしいにゃ🧶".isNekogo(mockContext)).isTrue()
    }

    @Test
    fun isNekogo_type1単体とはっしゅたぐ2つがtrue() {
        Truth.assertThat("にゃ1 #なでてほしいにゃ🧶 #にゃんにゃんエンジン".isNekogo(mockContext)).isTrue()
    }

    @Test
    fun isNekogo_type1単体と未登録はっしゅたぐ1つがfalse() {
        Truth.assertThat("にゃ1 #にゃんにゃんのエンジン".isNekogo(mockContext)).isFalse()
    }
}
