package com.ntetz.android.nyannyanengine_android.ui.sign_out

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessTokenInvalidation
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import kotlinx.coroutines.launch

class SignOutViewModel(private val accountUsecase: IAccountUsecase) : ViewModel() {
    private val _signOutEvent: MutableLiveData<AccessTokenInvalidation?> = MutableLiveData()
    val signOutEvent: LiveData<AccessTokenInvalidation?>
        get() = _signOutEvent

    fun executeSignOut() {
        viewModelScope.launch {
            _signOutEvent.postValue(
                accountUsecase.deleteAccessToken(this)
            )
        }
    }
}
