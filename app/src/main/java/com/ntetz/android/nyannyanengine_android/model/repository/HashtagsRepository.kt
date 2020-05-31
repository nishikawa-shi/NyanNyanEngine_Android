package com.ntetz.android.nyannyanengine_android.model.repository

import androidx.lifecycle.LiveData
import com.ntetz.android.nyannyanengine_android.model.dao.room.DefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtag

class HashtagsRepository(
    private val defaultHashtagsDao: DefaultHashtagsDao,
    val allDefaultHashtags: LiveData<List<DefaultHashtag>> = defaultHashtagsDao.getAll()
)
