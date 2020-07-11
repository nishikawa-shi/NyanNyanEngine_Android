package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.usecase.IHashtagUsecase
import kotlinx.coroutines.launch

class HashtagSettingViewModel(private val hashtagUsecase: IHashtagUsecase) : ViewModel() {
    private val _defaultHashtagComponents: MutableLiveData<List<DefaultHashTagComponent>> = MutableLiveData(listOf())
    val defaultHashtagComponents: LiveData<List<DefaultHashTagComponent>>
        get() = _defaultHashtagComponents

    fun initialize() {
        viewModelScope.launch {
            _defaultHashtagComponents.postValue(hashtagUsecase.getDefaultHashtags(this))
        }
    }

    fun updateDefaultHashtagComponent(defaultHashtagComponent: DefaultHashTagComponent) {
        hashtagUsecase.updateDefaultHashtag(defaultHashtagComponent, viewModelScope)
    }
}
