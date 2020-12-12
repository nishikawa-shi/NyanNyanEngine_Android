package com.ntetz.android.nyannyanengine_android.model.dao.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord

@Dao
interface ITwitterUserDao {
    @Query("SELECT * FROM twitter_users")
    fun getAll(): List<TwitterUserRecord>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun upsert(twitterUserRecord: TwitterUserRecord)
}
