package com.ntetz.android.nyannyanengine_android.model.repository

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IHashtagsRepository {
    suspend fun getDefaultHashtags(scope: CoroutineScope, context: Context): List<DefaultHashTagComponent>
    fun updateDefaultHashtagRecord(record: DefaultHashtagRecord, scope: CoroutineScope)
}

class HashtagsRepository(
    private val defaultHashtagConfig: IDefaultHashtagConfig,
    private val defaultHashtagsDao: IDefaultHashtagsDao
) : IHashtagsRepository {

    override suspend fun getDefaultHashtags(scope: CoroutineScope, context: Context): List<DefaultHashTagComponent> {
        return getDefaultHashtagRecords(scope).map {
            DefaultHashTagComponent(
                it.id,
                context.getString(defaultHashtagConfig.getTextBodyId(it.id) ?: return emptyList()),
                defaultHashtagConfig.getNekosanPoint(it.id) ?: 0,
                it.enabled
            )
        }
    }

    override fun updateDefaultHashtagRecord(record: DefaultHashtagRecord, scope: CoroutineScope) {
        scope.launch {
            withContext(Dispatchers.IO) {
                defaultHashtagsDao.updateOne(record)
            }
        }
    }

    private suspend fun getDefaultHashtagRecords(scope: CoroutineScope): List<DefaultHashtagRecord> {
        return withContext(scope.coroutineContext) {
            withContext(Dispatchers.IO) {
                defaultHashtagsDao.getAll()
            }
        }
    }
}
