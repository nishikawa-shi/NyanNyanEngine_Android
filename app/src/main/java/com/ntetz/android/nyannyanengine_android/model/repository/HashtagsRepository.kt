package com.ntetz.android.nyannyanengine_android.model.repository

import androidx.lifecycle.LiveData
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord

interface IHashtagsRepository {
    val allDefaultHashtagRecords: LiveData<List<DefaultHashtagRecord>>
}

class HashtagsRepository(
    private val defaultHashtagsDao: IDefaultHashtagsDao,
    override val allDefaultHashtagRecords: LiveData<List<DefaultHashtagRecord>> = defaultHashtagsDao.allRecords()
) : IHashtagsRepository
