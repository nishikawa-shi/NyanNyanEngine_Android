package com.ntetz.android.nyannyanengine_android.util

import android.content.Context
import com.ntetz.android.nyannyanengine_android.R
import java.security.MessageDigest

fun String?.toNyanNyan(context: Context): String {
    if (this == null) {
        return context.getText(R.string.nekosan_nakigoe_type99).toString()
    }
    if (this.isEmpty()) {
        return ""
    }
    if (this.isNekogo(context)) {
        return this
    }
    val nekogoSource = getMD5(this)
    val nekogoBody =
        nekogoSource.take(3).fold("") { result, current -> result + getNekogoBodyFragment(current, context) }
    val nekogoPrefix =
        nekogoSource.takeLast(1).fold("") { result, current -> result + getNekogoSuffixFragment(current) }
    return nekogoBody + nekogoPrefix
}

fun String.isNekogo(context: Context): Boolean {
    val nekogoRenge = listOf(
        context.getString(R.string.nekosan_nakigoe_type1),
        context.getString(R.string.nekosan_nakigoe_type2),
        context.getString(R.string.nekosan_nakigoe_type3),
        context.getString(R.string.nekosan_nakigoe_type4),
        context.getString(R.string.nekosan_nakigoe_type5),
        context.getString(R.string.nekosan_nakigoe_type6),
        context.getString(R.string.nekosan_nakigoe_type7),
        context.getString(R.string.nekosan_nakigoe_type8),
        context.getString(R.string.nekosan_nakigoe_type9),
        context.getString(R.string.nekosan_nakigoe_type99)
    ).joinToString("|")
    val nekogoSuffixRange = "😊|🙁|🐤|🐳|🐟|🏆|🌈|🎊|:\\)|XD"
    val hashtagRenge = listOf(
        context.getString(R.string.settings_title_hashtag_engine),
        context.getString(R.string.settings_title_hashtag_nadenade)
    ).map { "\\s$it" }.joinToString("|")

    val pattern = """($nekogoRenge){1,3}($nekogoSuffixRange)?($hashtagRenge)*"""
    return Regex(pattern).matches(this)
}

fun String.nekosanPoint(): Int {
    return getNekosanPrefixPoint(this)
}

private fun getMD5(text: String): String {
    val digest = MessageDigest.getInstance("MD5").also { it.update(text.toByteArray()) }
    val messageDigest = digest.digest()
    return messageDigest.fold(initial = StringBuilder()) { result, current ->
        var h = Integer.toHexString(0xFF and current.toInt())
        while (h.length < 2) h = "0$h"
        result.also { it.append(h) }
    }.toString()
}

private fun getNekogoBodyFragment(char: Char, context: Context): String {
    return when (char) {
        '0' -> context.getString(R.string.nekosan_nakigoe_type1)
        '1' -> context.getString(R.string.nekosan_nakigoe_type1)
        '2' -> context.getString(R.string.nekosan_nakigoe_type2)
        '3' -> context.getString(R.string.nekosan_nakigoe_type2)
        '4' -> context.getString(R.string.nekosan_nakigoe_type3)
        '5' -> context.getString(R.string.nekosan_nakigoe_type3)
        '6' -> context.getString(R.string.nekosan_nakigoe_type4)
        '7' -> context.getString(R.string.nekosan_nakigoe_type4)
        '8' -> context.getString(R.string.nekosan_nakigoe_type4)
        '9' -> context.getString(R.string.nekosan_nakigoe_type5)
        'a' -> context.getString(R.string.nekosan_nakigoe_type5)
        'b' -> context.getString(R.string.nekosan_nakigoe_type6)
        'c' -> context.getString(R.string.nekosan_nakigoe_type7)
        'd' -> context.getString(R.string.nekosan_nakigoe_type8)
        'e' -> context.getString(R.string.nekosan_nakigoe_type9)
        'f' -> context.getString(R.string.nekosan_nakigoe_type9)
        else -> context.getString(R.string.nekosan_nakigoe_type99)
    }
}

private fun getNekogoSuffixFragment(char: Char): String {
    return when (char) {
        '0' -> ""
        '1' -> "😊"
        '2' -> "🙁"
        '3' -> ""
        '4' -> "🐤"
        '5' -> "🐳"
        '6' -> ""
        '7' -> "🐟"
        '8' -> "🐟"
        '9' -> "🏆"
        'a' -> ""
        'b' -> "🌈"
        'c' -> "🎊"
        'd' -> ""
        'e' -> ":)"
        'f' -> "XD"
        else -> "(^o^)"
    }
}

private fun getNekosanPrefixPoint(nekogoStr: String): Int {
    if (nekogoStr.contains("🌈")) {
        return 80
    }
    if (nekogoStr.contains("🐟")) {
        return 80
    }
    if (nekogoStr.contains("😊")) {
        return 80
    }
    if (nekogoStr.contains("🏆")) {
        return 50
    }
    if (nekogoStr.contains("🎊")) {
        return 50
    }
    if (nekogoStr.contains(":)")) {
        return 30
    }
    if (nekogoStr.contains("XD")) {
        return 30
    }
    if (nekogoStr.contains("🐤")) {
        return 20
    }
    if (nekogoStr.contains("🙁")) {
        return 20
    }
    return 10
}
