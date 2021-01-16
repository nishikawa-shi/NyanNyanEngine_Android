package com.ntetz.android.nyannyanengine_android

import android.app.Application
import com.ntetz.android.nyannyanengine_android.model.usecase.IApplicationUsecase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {
    @Inject
    lateinit var applicationUsecase: IApplicationUsecase

    override fun onCreate() {
        super.onCreate()
        applicationUsecase.launch()
    }
}
