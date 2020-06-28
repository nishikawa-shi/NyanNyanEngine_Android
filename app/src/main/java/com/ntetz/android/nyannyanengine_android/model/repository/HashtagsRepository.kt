package com.ntetz.android.nyannyanengine_android.model.repository

import androidx.lifecycle.LiveData
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtag

interface IHashtagsRepository {
    val allDefaultHashtags: LiveData<List<DefaultHashtag>>
}

class HashtagsRepository(
    private val defaultHashtagsDao: IDefaultHashtagsDao,
    override val allDefaultHashtags: LiveData<List<DefaultHashtag>> = defaultHashtagsDao.getAll()
) : IHashtagsRepository
