package com.ntetz.android.nyannyanengine_android.model.config

import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.model.dao.room.IDefaultHashtagsDao
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import javax.inject.Inject

interface IDefaultHashtagConfig {
    fun getInitializationRecords(): List<DefaultHashtagRecord>
    fun getTextBodyId(hashtagId: Int): Int?
    fun getNekosanPoint(hashtagId: Int): Int?
    suspend fun populate(defaultHashtagsDao: IDefaultHashtagsDao)
}

class DefaultHashtagConfig @Inject constructor() : IDefaultHashtagConfig {
    private val entries = mapOf(
        1 to DefaultHashtag(R.string.settings_title_hashtag_engine, true, 30),
        2 to DefaultHashtag(R.string.settings_title_hashtag_nadenade, false, 50)
    )

    override fun getInitializationRecords(): List<DefaultHashtagRecord> =
        entries.map { DefaultHashtagRecord(it.key, it.value.defaultEnabled) }

    override fun getTextBodyId(hashtagId: Int): Int? = entries[hashtagId]?.textBodyId

    override fun getNekosanPoint(hashtagId: Int): Int? = entries[hashtagId]?.nekosanPoint

    override suspend fun populate(defaultHashtagsDao: IDefaultHashtagsDao) {
        val registeredHashtagIds = defaultHashtagsDao.getAll().map { it.id }
        getInitializationRecords().forEach { initializationRecord ->
            if (!registeredHashtagIds.contains(initializationRecord.id)) {
                defaultHashtagsDao.insert(initializationRecord)
            }
        }
    }
}

private data class DefaultHashtag(
    val textBodyId: Int,
    val defaultEnabled: Boolean,
    val nekosanPoint: Int
)
