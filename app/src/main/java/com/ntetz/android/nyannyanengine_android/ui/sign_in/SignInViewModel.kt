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
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountUsecase: IAccountUsecase,
    private val userActionUsecase: IUserActionUsecase,
    @ApplicationContext context: Context
) : ViewModel() {
    private val context = WeakReference(context)
    private val _signInEvent: MutableLiveData<SignInResultComponent?> = MutableLiveData()
    val signInEvent: LiveData<SignInResultComponent?>
        get() = _signInEvent

    fun executeSignIn(oauthVerifier: String?, oauthToken: String?) {
        viewModelScope.launch {
            if (oauthVerifier == null || oauthToken == null) {
                _signInEvent.postValue(null)
                return@launch
            }
            _signInEvent.postValue(
                accountUsecase.fetchAccessToken(
                    oauthVerifier = oauthVerifier,
                    oauthToken = oauthToken,
                    scope = this,
                    context = context.get() ?: return@launch
                )
            )
            userActionUsecase.complete(userAction = UserAction.SIGN_IN, scope = this)
        }
    }
}
