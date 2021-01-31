package com.ntetz.android.nyannyanengine_android.model.entity.usecase.account

data class SignInResultComponent(
    val isSucceeded: Boolean,
    val errorMessage: String? = null
)
