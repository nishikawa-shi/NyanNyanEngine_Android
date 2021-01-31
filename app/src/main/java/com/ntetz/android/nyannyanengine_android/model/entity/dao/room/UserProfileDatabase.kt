package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.dao.room.ITwitterUserDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface IUserProfileDatabase {
    fun initialize(defaultHashtagConfig: IDefaultHashtagConfig)
    fun defaultHashtagsDao(): IDefaultHashtagsDao
    fun twitterUserDao(): ITwitterUserDao
}

@Database(
    entities = arrayOf(DefaultHashtagRecord::class, TwitterUserRecord::class),
    version = 1,
    exportSchema = false
)
abstract class UserProfileDatabase : RoomDatabase(), IUserProfileDatabase {
    abstract override fun defaultHashtagsDao(): IDefaultHashtagsDao
    abstract override fun twitterUserDao(): ITwitterUserDao

    override fun initialize(defaultHashtagConfig: IDefaultHashtagConfig) {
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
