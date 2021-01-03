package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserProfileDatabaseTests {
    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockDefaultHashtagConfig: IDefaultHashtagConfig

    @Before
    fun setUp() {
        startKoin {
            modules(module {
                single { mockDefaultHashtagConfig }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun initialize_mockDefaultHashtagConfigのpopulateが1回呼ばれること() = runBlocking {
        `when`(mockContext.applicationContext).thenReturn(mockContext)
        val database = UserProfileDatabase.getDatabase(mockContext)
        `when`(mockDefaultHashtagConfig.populate(database.defaultHashtagsDao())).thenReturn(Unit)

        database.initialize()
        delay(20) // これがないと、initialize内部のCoroutineの起動を見届けられない模様
        verify(mockDefaultHashtagConfig, times(1)).populate(database.defaultHashtagsDao())
    }
}
