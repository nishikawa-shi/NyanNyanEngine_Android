package com.ntetz.android.nyannyanengine_android

import org.mockito.Mockito

class TestUtil private constructor() {

    companion object {
        fun <T> any(): T {
            Mockito.any<T>()
            return nullReturn()
        }

        private fun <T> nullReturn(): T = null as T
    }
}
