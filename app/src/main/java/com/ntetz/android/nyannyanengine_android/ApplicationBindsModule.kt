package com.ntetz.android.nyannyanengine_android

import com.ntetz.android.nyannyanengine_android.model.config.DefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.FirebaseClient
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.repository.HashtagsRepository
import com.ntetz.android.nyannyanengine_android.model.repository.IHashtagsRepository
import com.ntetz.android.nyannyanengine_android.model.repository.IMetricsRepository
import com.ntetz.android.nyannyanengine_android.model.repository.MetricsRepository
import com.ntetz.android.nyannyanengine_android.model.usecase.AccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.HashtagUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IHashtagUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.TweetsUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.UserActionUsecase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationBindsModule {
    @Binds
    abstract fun bindFirebaseClient(firebaseClient: FirebaseClient): IFirebaseClient

    @Binds
    abstract fun bindDefaultHashtagConfig(defaultHashtagConfig: DefaultHashtagConfig): IDefaultHashtagConfig

    @Binds
    abstract fun bindHashtagsRepository(hashtagsRepository: HashtagsRepository): IHashtagsRepository

    @Binds
    abstract fun bindMetricsRepository(metricsRepository: MetricsRepository): IMetricsRepository

    @Binds
    abstract fun bindAccountUsecase(accountUsecase: AccountUsecase): IAccountUsecase

    @Binds
    abstract fun bindTweetsUsecase(tweetsUsecase: TweetsUsecase): ITweetsUsecase

    @Binds
    abstract fun bindHashtagUsecase(hashtagUsecase: HashtagUsecase): IHashtagUsecase

    @Binds
    abstract fun bindUserActionUsecase(userActionUsecase: UserActionUsecase): IUserActionUsecase
}
