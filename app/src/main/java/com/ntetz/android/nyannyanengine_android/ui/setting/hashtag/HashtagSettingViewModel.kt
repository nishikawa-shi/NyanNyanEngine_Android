package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtag
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.UserProfileDatabase
import com.ntetz.android.nyannyanengine_android.model.repository.HashtagsRepository

class HashtagSettingViewModel(private val context: Context) : ViewModel() {
    private val hashtagsRepository: HashtagsRepository
    val defaultHashtags: LiveData<List<DefaultHashtag>>

    init {
        val hashtagsDao = UserProfileDatabase.getDatabase(context, viewModelScope).defaultHashtagsDao()
        hashtagsRepository = HashtagsRepository(hashtagsDao)
        defaultHashtags = hashtagsRepository.allDefaultHashtags
    }
}
