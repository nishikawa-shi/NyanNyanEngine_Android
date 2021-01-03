package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.IUserProfileDatabase

interface IApplicationUsecase {
    fun launch()
}

class ApplicationUsecase(
    private val userProfileDatabase: IUserProfileDatabase,
    private val firebaseClient: IFirebaseClient
) : IApplicationUsecase {
    override fun launch() {
        userProfileDatabase.initialize()
        firebaseClient.initialize()
    }
}
