package com.ntetz.android.nyannyanengine_android

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.ITwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.retrofit.TwitterApi
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.dao.room.ITwitterUserDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.IUserProfileDatabase
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.UserProfileDatabase
import com.ntetz.android.nyannyanengine_android.model.repository.AccountRepository
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationProvidesModule {
    @Provides
    fun provideUserProfileDatabase(@ApplicationContext context: Context): IUserProfileDatabase =
        UserProfileDatabase.getDatabase(context)

    @Provides
    fun provideTwitterApi(@ApplicationContext context: Context): ITwitterApi = TwitterApi(context)

    @Provides
    fun provideTwitterUserDao(
        userProfileDatabase: IUserProfileDatabase
    ): ITwitterUserDao = userProfileDatabase.twitterUserDao()

    @Provides
    fun provideDefaultHashtagsDao(
        userProfileDatabase: IUserProfileDatabase
    ): IDefaultHashtagsDao = userProfileDatabase.defaultHashtagsDao()

    @Provides
    fun provideAccountRepository(
        twitterApi: ITwitterApi,
        twitterUserDao: ITwitterUserDao,
        firebaseClient: IFirebaseClient
    ): IAccountRepository = AccountRepository(
        twitterApiScalarClient = twitterApi.scalarClient,
        twitterApiObjectClient = twitterApi.objectClient,
        twitterUserDao = twitterUserDao,
        firebaseClient = firebaseClient
    )
}
