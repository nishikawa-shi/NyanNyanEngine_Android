package com.ntetz.android.nyannyanengine_android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// AndroidManifest.xmlからの参照を受けている。
@Suppress("unused")
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(MainModule.modules)
        }
    }
}
