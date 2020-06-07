package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTag
import com.ntetz.android.nyannyanengine_android.model.usecase.HashtagUsecase

class HashtagSettingViewModel(hashtagUsecase: HashtagUsecase) : ViewModel() {
    val defaultHashtags: LiveData<List<DefaultHashTag>> = hashtagUsecase.defaultHashtags
}
