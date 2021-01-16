package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.IUserProfileDatabase
import javax.inject.Inject

interface IApplicationUsecase {
    fun launch()
}

class ApplicationUsecase @Inject constructor(
    private val userProfileDatabase: IUserProfileDatabase,
    private val firebaseClient: IFirebaseClient,
    private val defaultHashtagConfig: IDefaultHashtagConfig
) : IApplicationUsecase {
    override fun launch() {
        userProfileDatabase.initialize(defaultHashtagConfig)
        firebaseClient.initialize()
    }
}
