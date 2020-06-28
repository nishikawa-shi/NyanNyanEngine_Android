package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ntetz.android.nyannyanengine_android.model.config.DefaultHashtag
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTag
import com.ntetz.android.nyannyanengine_android.model.repository.IHashtagsRepository

interface IHashtagUsecase {
    val defaultHashtags: LiveData<List<DefaultHashTag>>
}

class HashtagUsecase(
    private val hashtagsRepository: IHashtagsRepository,
    private val context: Context
) : IHashtagUsecase {
    override val defaultHashtags: LiveData<List<DefaultHashTag>>
        get() {
            return Transformations.map(hashtagsRepository.allDefaultHashtags) { savedList ->
                savedList.map { savedEntity ->
                    val textId = DefaultHashtag.values()
                        .first { it.id == savedEntity.id }
                        .textBodyId
                    DefaultHashTag(savedEntity.id, context.getString(textId), savedEntity.enabled)
                }
            }
        }
}
