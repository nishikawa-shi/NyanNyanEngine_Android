package com.ntetz.android.nyannyanengine_android

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.config.DefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.IUserProfileDatabase
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.UserProfileDatabase
import com.ntetz.android.nyannyanengine_android.model.repository.HashtagsRepository
import com.ntetz.android.nyannyanengine_android.model.usecase.ApplicationUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.HashtagUsecase
import com.ntetz.android.nyannyanengine_android.ui.main.MainViewModel
import com.ntetz.android.nyannyanengine_android.ui.post_nekogo.PostNekogoViewModel
import com.ntetz.android.nyannyanengine_android.ui.setting.hashtag.HashtagSettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MainModule {
    val modules = listOf(viewModelModule)
}

private val viewModelModule = module {
    single<IDefaultHashtagConfig> { DefaultHashtagConfig() }

    single { ApplicationUsecase(getUserProfileDatabase(androidContext())) }

    viewModel {
        val dao = getUserProfileDatabase(androidContext()).defaultHashtagsDao()
        val repository = HashtagsRepository(dao)
        val usecase = HashtagUsecase(repository, get(), androidContext())
        HashtagSettingViewModel(usecase)
    }
    viewModel { MainViewModel() }
    viewModel { PostNekogoViewModel() }
}

private fun getUserProfileDatabase(context: Context): IUserProfileDatabase = UserProfileDatabase.getDatabase(context)
