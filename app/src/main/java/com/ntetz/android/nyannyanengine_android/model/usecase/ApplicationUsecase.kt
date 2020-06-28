package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.UserProfileDatabase

class ApplicationUsecase(private val userProfileDatabase: UserProfileDatabase) {
    fun launch() {
        userProfileDatabase.initialize()
    }
}
