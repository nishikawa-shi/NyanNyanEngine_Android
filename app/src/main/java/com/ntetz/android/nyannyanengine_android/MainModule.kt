package com.ntetz.android.nyannyanengine_android

import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.UserProfileDatabase
import com.ntetz.android.nyannyanengine_android.model.repository.HashtagsRepository
import com.ntetz.android.nyannyanengine_android.ui.main.MainViewModel
import com.ntetz.android.nyannyanengine_android.ui.post_nekogo.PostNekogoViewModel
import com.ntetz.android.nyannyanengine_android.ui.setting.hashtag.HashtagSettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val viewModelModule = module {
    viewModel {
        val dao = UserProfileDatabase.getDatabase(androidContext()).defaultHashtagsDao()
        val repository = HashtagsRepository(dao)
        HashtagSettingViewModel(repository)
    }
    viewModel { MainViewModel() }
    viewModel { PostNekogoViewModel() }
}


object MainModule {
    val modules = listOf(viewModelModule)
}
