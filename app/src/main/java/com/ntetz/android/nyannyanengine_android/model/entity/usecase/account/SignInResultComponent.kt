package com.ntetz.android.nyannyanengine_android.model.entity.usecase.account

data class SignInResultComponent(
    val isSucceeded: Boolean,
    val errorCode: Int? = null,
    val errorMessage: String? = null
)
