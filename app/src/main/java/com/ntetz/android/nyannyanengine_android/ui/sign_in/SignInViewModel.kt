package com.ntetz.android.nyannyanengine_android.ui.sign_in

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.SignInResultComponent
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.screen_transition.UserAction
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
import kotlinx.coroutines.launch

class SignInViewModel(
    private val accountUsecase: IAccountUsecase,
    private val userActionUsecase: IUserActionUsecase,
    private val context: Context
) : ViewModel() {
    private val _signInEvent: MutableLiveData<SignInResultComponent?> = MutableLiveData()
    val signInEvent: LiveData<SignInResultComponent?>
        get() = _signInEvent

    fun executeSignIn(oauthVerifier: String?, oauthToken: String?) {
        viewModelScope.launch {
            if (oauthVerifier == null || oauthToken == null) {
                // TODO: 基本的には発生しないが、クエリストリングなしでアプリに遷移してきた時のエラーハンドリング
                _signInEvent.postValue(null)
                println("tootteruyo")
                return@launch
            }
            _signInEvent.postValue(
                accountUsecase.fetchAccessToken(
                    oauthVerifier = oauthVerifier,
                    oauthToken = oauthToken,
                    scope = this,
                    context = context
                )
            )
            userActionUsecase.complete(userAction = UserAction.SIGN_IN, scope = this)
        }
    }
}
