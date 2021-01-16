package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag.DefaultHashTagComponent
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.screen_transition.UserAction
import com.ntetz.android.nyannyanengine_android.model.usecase.IHashtagUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

class HashtagSettingViewModel @ViewModelInject constructor(
    private val hashtagUsecase: IHashtagUsecase,
    private val userActionUsecase: IUserActionUsecase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _defaultHashtagComponents: MutableLiveData<List<DefaultHashTagComponent>> = MutableLiveData(listOf())
    val defaultHashtagComponents: LiveData<List<DefaultHashTagComponent>>
        get() = _defaultHashtagComponents

    fun initialize() {
        viewModelScope.launch {
            _defaultHashtagComponents.postValue(hashtagUsecase.getDefaultHashtags(this, context))
        }
    }

    fun updateDefaultHashtagComponent(defaultHashtagComponent: DefaultHashTagComponent) {
        hashtagUsecase.updateDefaultHashtag(defaultHashtagComponent, viewModelScope, context)
        viewModelScope.launch {
            userActionUsecase.complete(
                userAction = UserAction.SETTING_HASH_TAG,
                textParams = mapOf(
                    "tag_body" to defaultHashtagComponent.textBody,
                    "is_enabled" to defaultHashtagComponent.isEnabled.toString()
                ),
                scope = this
            )
        }
    }
}
