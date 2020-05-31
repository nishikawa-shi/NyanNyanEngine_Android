package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ntetz.android.nyannyanengine_android.model.dao.room.DefaultHashtagsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(DefaultHashtag::class), version = 1, exportSchema = false)
abstract class UserProfileDatabase : RoomDatabase() {
    abstract fun defaultHashtagsDao(): DefaultHashtagsDao

    private class UserProfileDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.defaultHashtagsDao())
                }
            }
        }

        suspend fun populateDatabase(defaultHashtagsDao: DefaultHashtagsDao) {
            // DB初期化時に走るここの処理で、初期値が投入される。
            // TODO: まともな初期データを考える
            defaultHashtagsDao.insert(DefaultHashtag(1, true))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserProfileDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
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
                    .addCallback(UserProfileDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
