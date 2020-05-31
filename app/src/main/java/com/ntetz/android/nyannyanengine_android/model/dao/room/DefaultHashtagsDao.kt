package com.ntetz.android.nyannyanengine_android.model.dao.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtag

@Dao
interface DefaultHashtagsDao {
    @Query("SELECT * FROM default_hashtags")
    fun getAll(): LiveData<List<DefaultHashtag>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(defaultHashtag: DefaultHashtag)
}
