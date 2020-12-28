package com.ntetz.android.nyannyanengine_android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import kotlinx.coroutines.launch

class MainViewModel(private val tweetsUsecase: ITweetsUsecase) : ViewModel() {
    private val _tweetsEvent: MutableLiveData<List<Tweet>?> = MutableLiveData()
    private val _isLoadingFirstView: MutableLiveData<Boolean> = MutableLiveData()

    val tweetsEvent: LiveData<List<Tweet>?>
        get() = _tweetsEvent
    val isLoadingFirstView: LiveData<Boolean>
        get() = _isLoadingFirstView

    fun initialize() {
        viewModelScope.launch {
            _tweetsEvent.postValue(tweetsUsecase.getTweets(this))
        }
    }

    fun getLatestTweets() {
        viewModelScope.launch {
            _isLoadingFirstView.postValue(true)
            _tweetsEvent.postValue(tweetsUsecase.getLatestTweets(this))
            _isLoadingFirstView.postValue(false)
        }
    }
}
