package com.ntetz.android.nyannyanengine_android

import android.app.Application
import com.ntetz.android.nyannyanengine_android.model.usecase.IApplicationUsecase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidApp
class MainApplication : Application() {
    @Inject
    lateinit var applicationUsecase: IApplicationUsecase

    override fun onCreate() {
        super.onCreate()
        setDependency()
        applicationUsecase.launch()
    }

    private fun setDependency() {
        startKoin {
            androidContext(this@MainApplication)
            modules(MainModule.modules)
        }
    }
}
