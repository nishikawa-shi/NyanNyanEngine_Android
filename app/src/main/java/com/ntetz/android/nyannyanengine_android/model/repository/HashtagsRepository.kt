package com.ntetz.android.nyannyanengine_android.model.repository

import androidx.lifecycle.LiveData
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IHashtagsRepository {
    val allDefaultHashtagRecords: LiveData<List<DefaultHashtagRecord>>
    fun updateDefaultHashtagRecord(record: DefaultHashtagRecord, scope: CoroutineScope)
}

class HashtagsRepository(
    private val defaultHashtagsDao: IDefaultHashtagsDao,
    override val allDefaultHashtagRecords: LiveData<List<DefaultHashtagRecord>> = defaultHashtagsDao.allRecords()
) : IHashtagsRepository {

    override fun updateDefaultHashtagRecord(record: DefaultHashtagRecord, scope: CoroutineScope) {
        scope.launch {
            withContext(Dispatchers.IO) {
                defaultHashtagsDao.updateOne(record)
            }
        }
    }
}
