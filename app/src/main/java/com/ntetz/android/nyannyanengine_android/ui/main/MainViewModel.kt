package com.ntetz.android.nyannyanengine_android.ui.main

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.NyanNyanUserComponent
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.screen_transition.UserAction
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val accountUsecase: IAccountUsecase,
    private val tweetsUsecase: ITweetsUsecase,
    private val userActionUsecase: IUserActionUsecase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _userInfoEvent: MutableLiveData<TwitterUserRecord?> = MutableLiveData()
    val userInfoEvent: LiveData<TwitterUserRecord?>
        get() = _userInfoEvent

    val nyanNyanUserEvent: LiveData<NyanNyanUserComponent?> =
        accountUsecase.nyanNyanUserEvent
    val nyanNyanConfigEvent: LiveData<NyanNyanConfig?> = accountUsecase.nyanNyanConfigEvent

    val tweetStream: Flow<PagingData<Tweet>> = Pager(
        config = PagingConfig(
            pageSize = TwitterEndpoints.homeTimelineCountParamDefaultValue.toInt(),
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            TweetsPagingSource(
                scope = viewModelScope,
                context = context,
                tweetsUsecase = tweetsUsecase
            )
        }
    ).flow.cachedIn(viewModelScope)

    fun loadUserInfo() {
        viewModelScope.launch {
            _userInfoEvent.postValue(accountUsecase.loadAccessToken(this, context))
            accountUsecase.fetchNyanNyanConfig()
        }
    }

    fun loadNyanNyanUserInfo() {
        viewModelScope.launch {
            accountUsecase.fetchNyanNyanUser(accountUsecase.loadAccessToken(this, context), context)
        }
    }

    fun logOpenPostNekogoScreen() {
        viewModelScope.launch {
            userActionUsecase.tap(
                userAction = UserAction.POST_NEKOGO,
                textParams = mapOf("is_signed_in" to (_userInfoEvent.value != null).toString()),
                scope = this
            )
        }
    }

    fun logToggleTweet(position: Int, toNekogo: Boolean) {
        viewModelScope.launch {
            userActionUsecase.tap(
                userAction = UserAction.TWEET,
                textParams = mapOf(
                    "is_signed_in" to (_userInfoEvent.value != null).toString(),
                    "to_nekogo" to toNekogo.toString()
                ),
                numParams = mapOf("position" to position),
                scope = this
            )
        }
    }
}
