package com.ntetz.android.nyannyanengine_android.model.dao.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord

@Dao
interface IDefaultHashtagsDao {
    @Query("SELECT * FROM default_hashtags")
    fun allRecords(): LiveData<List<DefaultHashtagRecord>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(defaultHashtagRecord: DefaultHashtagRecord)
}
