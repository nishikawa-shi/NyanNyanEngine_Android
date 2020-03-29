package com.ntetz.android.nyannyanengine_android

import com.ntetz.android.nyannyanengine_android.ui.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val viewModelModule = module {
    viewModel { MainViewModel() }
}

object MainModule {
    val modules = listOf(viewModelModule)
}
