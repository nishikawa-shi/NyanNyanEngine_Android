package com.ntetz.android.nyannyanengine_android

import com.ntetz.android.nyannyanengine_android.model.config.DefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.FirebaseClient
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.repository.HashtagsRepository
import com.ntetz.android.nyannyanengine_android.model.repository.IHashtagsRepository
import com.ntetz.android.nyannyanengine_android.model.repository.IMetricsRepository
import com.ntetz.android.nyannyanengine_android.model.repository.MetricsRepository
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
}
