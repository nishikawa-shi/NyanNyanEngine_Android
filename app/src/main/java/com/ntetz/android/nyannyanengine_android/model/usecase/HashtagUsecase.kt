package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ntetz.android.nyannyanengine_android.model.config.DefaultHashtag
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTag
import com.ntetz.android.nyannyanengine_android.model.repository.HashtagsRepository

class HashtagUsecase(
    private val hashtagsRepository: HashtagsRepository,
    private val context: Context
) {
    val defaultHashtags: LiveData<List<DefaultHashTag>>
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
