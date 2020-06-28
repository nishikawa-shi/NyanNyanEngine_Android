package com.ntetz.android.nyannyanengine_android.model.config

import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord

interface IDefaultHashtagConfig {
    fun getInitializationRecords(): List<DefaultHashtagRecord>
    fun getTextBodyId(hashtagId: Int): Int?
}

class DefaultHashtagConfig : IDefaultHashtagConfig {
    private val entries = mapOf(
        1 to DefaultHashtag(R.string.settings_title_hashtag_engine, true),
        2 to DefaultHashtag(R.string.settings_title_hashtag_nadenade, false)
    )

    override fun getInitializationRecords(): List<DefaultHashtagRecord> =
        entries.map { DefaultHashtagRecord(it.key, it.value.defaultEnabled) }

    override fun getTextBodyId(hashtagId: Int): Int? = entries[hashtagId]?.textBodyId
}

private data class DefaultHashtag(
    val textBodyId: Int,
    val defaultEnabled: Boolean
)