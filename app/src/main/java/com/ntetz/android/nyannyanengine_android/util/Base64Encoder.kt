package com.ntetz.android.nyannyanengine_android.util

import android.util.Base64

interface IBase64Encoder {
    fun encodeToStringForHttp(byteArray: ByteArray): String
}

// android.util.Base64直接利用だと、instrumented環境がテスト時に必要になってしまうのでラッパークラスを立てている
class Base64Encoder : IBase64Encoder {
    override fun encodeToStringForHttp(byteArray: ByteArray): String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
}
