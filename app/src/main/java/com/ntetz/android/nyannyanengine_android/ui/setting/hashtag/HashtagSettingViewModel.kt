package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtag
import com.ntetz.android.nyannyanengine_android.model.repository.HashtagsRepository

class HashtagSettingViewModel(hashtagsRepository: HashtagsRepository) : ViewModel() {
    // TODO: UseCaseとか噛ませたられた方が良さそう
    val defaultHashtags: LiveData<List<DefaultHashtag>> = hashtagsRepository.allDefaultHashtags
}
