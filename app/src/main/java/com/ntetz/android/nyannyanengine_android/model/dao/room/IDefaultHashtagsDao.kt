package com.ntetz.android.nyannyanengine_android.model.dao.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord

@Dao
interface IDefaultHashtagsDao {
    @Query("SELECT * FROM default_hashtags")
    fun getAll(): List<DefaultHashtagRecord>

    @Update
    fun updateOne(defaultHashtagRecord: DefaultHashtagRecord)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(defaultHashtagRecord: DefaultHashtagRecord)
}
