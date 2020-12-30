package com.ntetz.android.nyannyanengine_android.ui.post_nekogo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import kotlinx.coroutines.launch

class PostNekogoViewModel(private val tweetsUsecase: ITweetsUsecase) : ViewModel() {
    private val _postTweetEvent: MutableLiveData<Tweet?> = MutableLiveData()
    val postTweetEvent: LiveData<Tweet?>
        get() = _postTweetEvent

    fun postNekogo(inputText: String) {
        viewModelScope.launch {
            _postTweetEvent.postValue(tweetsUsecase.postTweet(inputText, this))
        }
    }
}
