package com.ntetz.android.nyannyanengine_android.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ntetz.android.nyannyanengine_android.model.config.TwitterEndpoints
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.usecase.ITweetsUsecase
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val tweetsUsecase: ITweetsUsecase) : ViewModel() {
    val tweetStream: Flow<PagingData<Tweet>> = Pager(
        config = PagingConfig(
            pageSize = TwitterEndpoints.homeTimelineCountParamDefaultValue.toInt(),
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TweetsPagingSource(scope = viewModelScope, tweetsUsecase = tweetsUsecase) }
    ).flow.cachedIn(viewModelScope)
}
