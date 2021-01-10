package com.ntetz.android.nyannyanengine_android.model.entity.usecase.account

import android.content.Context
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanUser

data class NyanNyanUserComponent(
    val nyanNyanUser: NyanNyanUser,
    private val nyanNyanConfig: NyanNyanConfig
) {
    fun getCurrentRank(context: Context): String? {
        nyanNyanConfig.ranks.keys.sortedByDescending { it }.forEach {
            val required = nyanNyanConfig.ranks[it]?.point ?: return null
            if ((nyanNyanUser.nekosanPoint) >= required) {
                return nyanNyanConfig.ranks[it]?.name
            }
        }
        return context.getString(R.string.settings_lowest_rank)
    }

    val currentExtends: String
        get() {
            val emptyValue = "-"
            nyanNyanConfig.ranks.keys.sortedBy { it }.forEach {
                val required = nyanNyanConfig.ranks[it]?.point ?: return emptyValue
                if (nyanNyanUser.nekosanPoint < required) {
                    return (required - nyanNyanUser.nekosanPoint).toString()
                }
            }
            return emptyValue
        }
}
