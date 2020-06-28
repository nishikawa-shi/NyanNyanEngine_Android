package com.ntetz.android.nyannyanengine_android.model.entity.dao.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "default_hashtags")
data class DefaultHashtag(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "enabled") val enabled: Boolean
)
