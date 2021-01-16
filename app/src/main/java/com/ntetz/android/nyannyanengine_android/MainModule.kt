package com.ntetz.android.nyannyanengine_android

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.config.DefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.FirebaseClient
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.TwitterApi
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.IUserProfileDatabase
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.UserProfileDatabase
import com.ntetz.android.nyannyanengine_android.model.repository.AccountRepository
import com.ntetz.android.nyannyanengine_android.model.repository.HashtagsRepository
import com.ntetz.android.nyannyanengine_android.model.repository.MetricsRepository
import com.ntetz.android.nyannyanengine_android.model.repository.TweetsRepository
import com.ntetz.android.nyannyanengine_android.model.usecase.AccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.ApplicationUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.HashtagUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.TweetsUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.UserActionUsecase
import com.ntetz.android.nyannyanengine_android.ui.main.MainViewModel
import com.ntetz.android.nyannyanengine_android.ui.post_nekogo.PostNekogoViewModel
import com.ntetz.android.nyannyanengine_android.ui.setting.hashtag.HashtagSettingViewModel
import com.ntetz.android.nyannyanengine_android.ui.sign_in.SignInViewModel
import com.ntetz.android.nyannyanengine_android.ui.sign_out.SignOutViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MainModule {
    val modules = listOf(viewModelModule)
}

private val viewModelModule = module {
    single<IDefaultHashtagConfig> { DefaultHashtagConfig() }
    single<IFirebaseClient> { FirebaseClient() }

    single { ApplicationUsecase(getUserProfileDatabase(androidContext()), get()) }
    single<IAccountUsecase> {
        val repository = AccountRepository(
            twitterApi = TwitterApi,
            twitterUserDao = getUserProfileDatabase(androidContext()).twitterUserDao(),
            firebaseClient = get()
        )
        AccountUsecase(repository)
    }
    single<IUserActionUsecase> {
        val metricsRepository = MetricsRepository(get())
        UserActionUsecase(metricsRepository)
    }
    single<ITweetsUsecase> {
        val tweetRepository = TweetsRepository(
            twitterApi = TwitterApi
        )
        val accountRepository = AccountRepository(
            twitterApi = TwitterApi,
            twitterUserDao = getUserProfileDatabase(androidContext()).twitterUserDao(),
            firebaseClient = get()
        )
        val hashtagsRepository =
            HashtagsRepository(get(), getUserProfileDatabase(androidContext()).defaultHashtagsDao())
        TweetsUsecase(
            tweetsRepository = tweetRepository,
            accountRepository = accountRepository,
            hashtagsRepository = hashtagsRepository
        )
    }

    viewModel {
        val dao = getUserProfileDatabase(androidContext()).defaultHashtagsDao()
        val repository = HashtagsRepository(get(), dao)
        val usecase = HashtagUsecase(repository)
        HashtagSettingViewModel(usecase, get(), androidContext())
    }
    viewModel { MainViewModel(get(), get(), get(), androidContext()) }
    viewModel { SignInViewModel(get(), get(), androidContext()) }
    viewModel { SignOutViewModel(get(), get(), androidContext()) }
    viewModel { PostNekogoViewModel(get(), get(), get(), androidContext()) }
}

private fun getUserProfileDatabase(context: Context): IUserProfileDatabase = UserProfileDatabase.getDatabase(context)
