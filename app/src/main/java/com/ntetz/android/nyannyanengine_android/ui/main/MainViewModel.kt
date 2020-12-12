package com.ntetz.android.nyannyanengine_android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import kotlinx.coroutines.launch

class MainViewModel(private val accountUsecase: IAccountUsecase) : ViewModel() {
    private val _twitterUserEvent: MutableLiveData<TwitterUserRecord?> =
        MutableLiveData()

    val twitterUserEvent: LiveData<TwitterUserRecord?>
        get() = _twitterUserEvent

    fun initialize() {
        viewModelScope.launch {
            _twitterUserEvent.postValue(accountUsecase.loadAccessToken(this))
        }
    }
}
