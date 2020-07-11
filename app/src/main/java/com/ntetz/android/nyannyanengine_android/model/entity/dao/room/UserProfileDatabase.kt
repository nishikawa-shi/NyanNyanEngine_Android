package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

interface IUserProfileDatabase {
    fun initialize()
    fun defaultHashtagsDao(): IDefaultHashtagsDao
}

@Database(entities = arrayOf(DefaultHashtagRecord::class), version = 1, exportSchema = false)
abstract class UserProfileDatabase : RoomDatabase(), KoinComponent, IUserProfileDatabase {
    abstract override fun defaultHashtagsDao(): IDefaultHashtagsDao
    private val defaultHashtagConfig: IDefaultHashtagConfig by inject()

    override fun initialize() {
        INSTANCE?.let { database ->
            GlobalScope.launch {
                defaultHashtagConfig.populate(database.defaultHashtagsDao())
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserProfileDatabase? = null

        fun getDatabase(
            context: Context
        ): UserProfileDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        UserProfileDatabase::class.java,
                        "user_profile_database"
                    )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
