package com.ntetz.android.nyannyanengine_android.model.repository

import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IHashtagsRepository {
    suspend fun getDefaultHashtagRecords(scope: CoroutineScope): List<DefaultHashtagRecord>
    fun updateDefaultHashtagRecord(record: DefaultHashtagRecord, scope: CoroutineScope)
}

class HashtagsRepository(
    private val defaultHashtagsDao: IDefaultHashtagsDao
) : IHashtagsRepository {

    override suspend fun getDefaultHashtagRecords(scope: CoroutineScope): List<DefaultHashtagRecord> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                defaultHashtagsDao.getAll()
            }
        }
    }

    override fun updateDefaultHashtagRecord(record: DefaultHashtagRecord, scope: CoroutineScope) {
        scope.launch {
            withContext(Dispatchers.IO) {
                defaultHashtagsDao.updateOne(record)
            }
        }
    }
}
