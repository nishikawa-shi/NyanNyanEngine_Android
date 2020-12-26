package com.ntetz.android.nyannyanengine_android.model.dao.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.CachedTweetRecord

@Dao
interface ICachedTweetsDao {
    @Query("SELECT * FROM cached_tweets")
    fun getAll(): List<CachedTweetRecord>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun upsert(cachedTweetRecords: List<CachedTweetRecord>)
}
