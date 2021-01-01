package com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase

enum class AnalyticsEvent(val title: String) {
    TAP_POST_NEKOGO("tap__post_nekogo"),
    COMPLETE_POST_NEKOGO("complete__post_nekogo"),

    TAP_SIGN_IN("tap__sign_in"),
    COMPLETE_SIGN_IN("complete__sign_in"),

    TAP_SIGN_OUT("tap__sign_out"),
    COMPLETE_SIGN_OUT("complete__sign_out"),

    TAP_SETTING_HASHTAG("tap__setting_hashtag"),
    COMPLETE_SETTING_HASHTAG("complete__setting_hashtag"),

    TAP_TWEET("tap__tweet"),
}
