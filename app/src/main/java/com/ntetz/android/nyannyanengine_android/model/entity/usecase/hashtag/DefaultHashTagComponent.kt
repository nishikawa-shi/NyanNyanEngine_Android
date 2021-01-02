package com.ntetz.android.nyannyanengine_android.model.entity.usecase.hashtag

data class DefaultHashTagComponent(
    val id: Int,
    val textBody: String,
    val nekosanPoint: Int = 0,
    val isEnabled: Boolean
)
