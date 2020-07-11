package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.usecase.IHashtagUsecase

class HashtagSettingViewModel(private val hashtagUsecase: IHashtagUsecase) : ViewModel() {
    val defaultHashtagComponents: LiveData<List<DefaultHashTagComponent>> = hashtagUsecase.defaultHashtagComponents
    fun updateDefaultHashtagComponent(defaultHashtagComponent: DefaultHashTagComponent) {
        hashtagUsecase.updateDefaultHashtag(defaultHashtagComponent, viewModelScope)
    }
}
