package com.ntetz.android.nyannyanengine_android.model.usecase

import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.dao.firebase.IFirebaseClient
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.IUserProfileDatabase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ApplicationUsecaseTests {
    @Mock
    private lateinit var mockUserProfileDatabase: IUserProfileDatabase

    @Mock
    private lateinit var mockFirebaseClient: IFirebaseClient

    @Mock
    private lateinit var mockDefaultHashtagConfig: IDefaultHashtagConfig

    @Test
    fun launch_userProfileDatabaseのinitializeが1度呼ばれること() {
        doNothing().`when`(mockUserProfileDatabase).initialize(mockDefaultHashtagConfig)

        ApplicationUsecase(mockUserProfileDatabase, mockFirebaseClient, mockDefaultHashtagConfig).launch()
        verify(mockUserProfileDatabase, times(1)).initialize(mockDefaultHashtagConfig)
    }

    @Test
    fun launch_firebaseClientのinitializeが1度呼ばれること() {
        doNothing().`when`(mockFirebaseClient).initialize()

        ApplicationUsecase(mockUserProfileDatabase, mockFirebaseClient, mockDefaultHashtagConfig).launch()
        verify(mockFirebaseClient, times(1)).initialize()
    }
}
