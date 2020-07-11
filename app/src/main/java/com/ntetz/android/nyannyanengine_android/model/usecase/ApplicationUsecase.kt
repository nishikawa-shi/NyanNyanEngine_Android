package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.IUserProfileDatabase

interface IApplicationUsecase {
    fun launch()
}

class ApplicationUsecase(private val userProfileDatabase: IUserProfileDatabase) : IApplicationUsecase {
    override fun launch() {
        userProfileDatabase.initialize()
    }
}
