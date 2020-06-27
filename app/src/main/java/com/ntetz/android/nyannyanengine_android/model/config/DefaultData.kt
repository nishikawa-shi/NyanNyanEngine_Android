package com.ntetz.android.nyannyanengine_android.model.config

import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.DefaultHashtag

object DefaultData {
    val hashTags: List<DefaultHashtag> = listOf(
        DefaultHashtag(1, true),
        DefaultHashtag(2, false)
    )
}
