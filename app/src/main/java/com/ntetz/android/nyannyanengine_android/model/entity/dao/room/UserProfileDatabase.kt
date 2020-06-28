package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ntetz.android.nyannyanengine_android.model.config.DefaultData
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(DefaultHashtag::class), version = 1, exportSchema = false)
abstract class UserProfileDatabase : RoomDatabase() {
    abstract fun defaultHashtagsDao(): IDefaultHashtagsDao

    fun initialize() {
        INSTANCE?.let { database ->
            GlobalScope.launch {
                populate(database.defaultHashtagsDao())
            }
        }
    }

    private suspend fun populate(defaultHashtagsDao: IDefaultHashtagsDao) {
        DefaultData.hashTags.forEach { defaultHashtagsDao.insert(it) }
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
