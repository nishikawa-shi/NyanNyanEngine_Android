package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.UserProfileDatabase

interface IApplicationUsecase {
    fun launch()
}

class ApplicationUsecase(private val userProfileDatabase: UserProfileDatabase) : IApplicationUsecase {
    override fun launch() {
        userProfileDatabase.initialize()
    }
}
