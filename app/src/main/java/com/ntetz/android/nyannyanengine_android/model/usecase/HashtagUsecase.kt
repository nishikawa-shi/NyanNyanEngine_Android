package com.ntetz.android.nyannyanengine_android.model.usecase

import android.content.Context
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtagRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.repository.IHashtagsRepository
import kotlinx.coroutines.CoroutineScope

interface IHashtagUsecase {
    suspend fun getDefaultHashtags(scope: CoroutineScope, context: Context): List<DefaultHashTagComponent>
    fun updateDefaultHashtag(component: DefaultHashTagComponent, scope: CoroutineScope, context: Context)
}

class HashtagUsecase(
    private val hashtagsRepository: IHashtagsRepository
) : IHashtagUsecase {
    override suspend fun getDefaultHashtags(scope: CoroutineScope, context: Context): List<DefaultHashTagComponent> {
        return hashtagsRepository.getDefaultHashtags(scope, context)
    }

    override fun updateDefaultHashtag(component: DefaultHashTagComponent, scope: CoroutineScope, context: Context) {
        hashtagsRepository.updateDefaultHashtagRecord(createDefaultHashtagRecord(component), scope)
    }

    private fun createDefaultHashtagRecord(component: DefaultHashTagComponent): DefaultHashtagRecord {
        return DefaultHashtagRecord(
            id = component.id,
            enabled = component.isEnabled
        )
    }
}
