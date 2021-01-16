package com.ntetz.android.nyannyanengine_android

import android.app.Application
import com.ntetz.android.nyannyanengine_android.model.usecase.ApplicationUsecase
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// AndroidManifest.xmlからの参照を受けている。
@Suppress("unused")
@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setDependency()
        inject<ApplicationUsecase>().value.launch()
    }

    private fun setDependency() {
        startKoin {
            androidContext(this@MainApplication)
            modules(MainModule.modules)
        }
    }
}
