package com.ntetz.android.nyannyanengine_android.model.dao.retrofit

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = "No Internet Connection"
}
