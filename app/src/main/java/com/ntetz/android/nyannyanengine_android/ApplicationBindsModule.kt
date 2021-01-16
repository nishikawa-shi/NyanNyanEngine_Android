package com.ntetz.android.nyannyanengine_android

import com.ntetz.android.nyannyanengine_android.model.dao.firebase.FirebaseClient
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationBindsModule {
    @Binds
    abstract fun bindFirebaseClient(firebaseClient: FirebaseClient): IFirebaseClient
}
