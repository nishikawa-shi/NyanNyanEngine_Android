package com.ntetz.android.nyannyanengine_android

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.IUserProfileDatabase
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.UserProfileDatabase
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
    fun provideDefaultHashtagsDao(
        userProfileDatabase: IUserProfileDatabase
    ): IDefaultHashtagsDao = userProfileDatabase.defaultHashtagsDao()
}
