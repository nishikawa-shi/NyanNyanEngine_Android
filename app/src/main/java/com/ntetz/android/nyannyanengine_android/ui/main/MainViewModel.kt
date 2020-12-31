package com.ntetz.android.nyannyanengine_android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val accountUsecase: IAccountUsecase,
    private val tweetsUsecase: ITweetsUsecase
) : ViewModel() {
    private val _userInfoEvent: MutableLiveData<TwitterUserRecord?> = MutableLiveData()
    val userInfoEvent: LiveData<TwitterUserRecord?>
        get() = _userInfoEvent

    val tweetStream: Flow<PagingData<Tweet>> = Pager(
        config = PagingConfig(
            pageSize = TwitterEndpoints.homeTimelineCountParamDefaultValue.toInt(),
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TweetsPagingSource(scope = viewModelScope, tweetsUsecase = tweetsUsecase) }
    ).flow.cachedIn(viewModelScope)

    fun loadUserInfo() {
        viewModelScope.launch {
            _userInfoEvent.postValue(accountUsecase.loadAccessToken(this))
        }
    }
}
