package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ntetz.android.nyannyanengine_android.model.config.IDefaultHashtagConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.repository.IHashtagsRepository

interface IHashtagUsecase {
    val defaultHashtagComponents: LiveData<List<DefaultHashTagComponent>>
}

class HashtagUsecase(
    private val hashtagsRepository: IHashtagsRepository,
    private val defaultHashtagConfig: IDefaultHashtagConfig,
    private val context: Context
) : IHashtagUsecase {
    override val defaultHashtagComponents: LiveData<List<DefaultHashTagComponent>>
        get() {
            return Transformations.map(hashtagsRepository.allDefaultHashtagRecords) { savedList ->
                savedList.mapNotNull {
                    createDefaultHashtagComponent(it)
                }
            }
        }

    private fun createDefaultHashtagComponent(record: DefaultHashtagRecord): DefaultHashTagComponent? {
        return DefaultHashTagComponent(
            id = record.id,
            textBody = context.getString(defaultHashtagConfig.getTextBodyId(record.id) ?: return null),
            isEnabled = record.enabled
        )
    }
}
