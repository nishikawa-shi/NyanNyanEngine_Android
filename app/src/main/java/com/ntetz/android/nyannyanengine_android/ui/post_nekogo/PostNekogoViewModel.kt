package com.ntetz.android.nyannyanengine_android.ui.post_nekogo

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.screen_transition.UserAction
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

class PostNekogoViewModel @ViewModelInject constructor(
    private val accountUsecase: IAccountUsecase,
    private val tweetsUsecase: ITweetsUsecase,
    private val userActionUsecase: IUserActionUsecase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _userInfoEvent: MutableLiveData<TwitterUserRecord?> = MutableLiveData()
    val userInfoEvent: LiveData<TwitterUserRecord?>
        get() = _userInfoEvent

    private val _postTweetEvent: MutableLiveData<Tweet?> = MutableLiveData()
    val postTweetEvent: LiveData<Tweet?>
        get() = _postTweetEvent

    fun loadUserInfo() {
        viewModelScope.launch {
            _userInfoEvent.postValue(accountUsecase.loadAccessToken(this, context))
        }
    }

    fun postNekogo(inputText: String, context: Context) {
        viewModelScope.launch {
            _postTweetEvent.postValue(tweetsUsecase.postTweet(inputText, this, context))
            userActionUsecase.complete(
                userAction = UserAction.POST_NEKOGO,
                textParams = mapOf("is_signed_in" to (_userInfoEvent.value != null).toString()),
                scope = this
            )
        }
    }
}
