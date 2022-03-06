package com.ntetz.android.nyannyanengine_android.ui.sign_out

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.AccessTokenInvalidation
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.screen_transition.UserAction
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val accountUsecase: IAccountUsecase,
    private val userActionUsecase: IUserActionUsecase
) : ViewModel() {
    private val _signOutEvent: MutableLiveData<AccessTokenInvalidation?> = MutableLiveData()
    val signOutEvent: LiveData<AccessTokenInvalidation?>
        get() = _signOutEvent

    fun executeSignOut() {
        viewModelScope.launch {
            _signOutEvent.postValue(
                accountUsecase.deleteAccessToken(this)
            )
            userActionUsecase.complete(userAction = UserAction.SIGN_OUT, scope = this)
        }
    }
}
