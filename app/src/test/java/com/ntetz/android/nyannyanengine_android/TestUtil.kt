package com.ntetz.android.nyannyanengine_android

import com.ntetz.android.nyannyanengine_android.util.IBase64Encoder
import java.util.*
import org.mockito.Mockito

class TestUtil private constructor() {

    companion object {
        val mockBase64Encoder: IBase64Encoder = MockBase64Encoder()

        fun <T> any(): T {
            Mockito.any<T>()
            return nullReturn()
        }

        private fun <T> nullReturn(): T = null as T
    }
}

private class MockBase64Encoder : IBase64Encoder {
    override fun encodeToStringForHttp(byteArray: ByteArray): String {
        val encoder = Base64.getUrlEncoder()
        return encoder.encodeToString(byteArray)
    }
}
