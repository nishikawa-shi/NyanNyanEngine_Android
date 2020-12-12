package com.ntetz.android.nyannyanengine_android.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.SignInResultComponent
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import kotlinx.coroutines.launch

class SignInViewModel(private val accountUsecase: IAccountUsecase) : ViewModel() {
    private val _signInEvent: MutableLiveData<SignInResultComponent?> = MutableLiveData()
    val signInEvent: LiveData<SignInResultComponent?>
        get() = _signInEvent

    fun executeSignIn() {
        viewModelScope.launch {
            _signInEvent.postValue(accountUsecase.fetchAccessToken())
        }
    }
}
