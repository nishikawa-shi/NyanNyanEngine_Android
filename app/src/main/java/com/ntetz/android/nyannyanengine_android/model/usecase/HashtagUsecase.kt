package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.repository.IHashtagsRepository
import kotlinx.coroutines.CoroutineScope

interface IHashtagUsecase {
    suspend fun getDefaultHashtags(scope: CoroutineScope): List<DefaultHashTagComponent>
    fun updateDefaultHashtag(component: DefaultHashTagComponent, scope: CoroutineScope)
}

class HashtagUsecase(
    private val hashtagsRepository: IHashtagsRepository,
    private val context: Context
) : IHashtagUsecase {
    override suspend fun getDefaultHashtags(scope: CoroutineScope): List<DefaultHashTagComponent> {
        return hashtagsRepository.getDefaultHashtags(scope, context)
    }

    override fun updateDefaultHashtag(component: DefaultHashTagComponent, scope: CoroutineScope) {
        hashtagsRepository.updateDefaultHashtagRecord(createDefaultHashtagRecord(component), scope)
    }

    private fun createDefaultHashtagRecord(component: DefaultHashTagComponent): DefaultHashtagRecord {
        return DefaultHashtagRecord(
            id = component.id,
            enabled = component.isEnabled
        )
    }
}
