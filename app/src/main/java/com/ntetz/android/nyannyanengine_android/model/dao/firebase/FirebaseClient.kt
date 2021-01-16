package com.ntetz.android.nyannyanengine_android.model.dao.firebase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ntetz.android.nyannyanengine_android.model.config.DefaultUserConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.AnalyticsEvent
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanRank
import com.ntetz.android.nyannyanengine_android.model.entity.dao.firebase.NyanNyanUser
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

interface IFirebaseClient {
    val nyanNyanConfigEvent: LiveData<NyanNyanConfig?>
    val nyanNyanUserEvent: LiveData<NyanNyanUser?>

    fun initialize()
    fun logEvent(type: AnalyticsEvent, textParams: Map<String, String>?, numParams: Map<String, Int>?)
    fun fetchNyanNyanUser(twitterUserRecord: TwitterUserRecord, context: Context)
    fun incrementNyanNyanUser(key: String, value: Int, twitterUserRecord: TwitterUserRecord)
    fun fetchNyanNyanConfig()
}

class FirebaseClient @Inject constructor() : IFirebaseClient {
    private val analytics = Firebase.analytics
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()

    private val usersCollectionName = "users"
    private val configCollectionName = "config"

    private val _nyanNyanConfigEvent = MutableLiveData<NyanNyanConfig?>()
    override val nyanNyanConfigEvent: LiveData<NyanNyanConfig?>
        get() = _nyanNyanConfigEvent

    private val _nyanNyanUserEvent = MutableLiveData<NyanNyanUser?>()
    override val nyanNyanUserEvent: LiveData<NyanNyanUser?>
        get() = _nyanNyanUserEvent

    override fun initialize() {
        auth.signInAnonymously()
    }

    override fun logEvent(type: AnalyticsEvent, textParams: Map<String, String>?, numParams: Map<String, Int>?) {
        analytics.logEvent(type.title) {
            textParams?.forEach { param(it.key, it.value) }
            numParams?.forEach { param(it.key, it.value.toLong()) }
        }
    }

    override fun fetchNyanNyanUser(twitterUserRecord: TwitterUserRecord, context: Context) {
        if (twitterUserRecord == DefaultUserConfig.getNotSignInUser(context)) {
            _nyanNyanUserEvent.postValue(
                DefaultUserConfig.getNotSignInNyanNyanUser(context)
            )
            return
        }
        db.collection(usersCollectionName).document(twitterUserRecord.sealedUserId)
            .get()
            .addOnSuccessListener {
                val data = it.data ?: return@addOnSuccessListener createNyanNyanUser(twitterUserRecord)

                val nekosanPoint = (data["np"] as? Long)?.toInt() ?: return@addOnSuccessListener
                val tweetCount = (data["tc"] as? Long)?.toInt() ?: return@addOnSuccessListener
                _nyanNyanUserEvent.postValue(
                    NyanNyanUser(id = it.id, nekosanPoint = nekosanPoint, tweetCount = tweetCount)
                )
            }
    }

    override fun incrementNyanNyanUser(key: String, value: Int, twitterUserRecord: TwitterUserRecord) {
        db.collection(usersCollectionName).document(twitterUserRecord.sealedUserId)
            .update(key, FieldValue.increment(value.toLong()))
    }

    override fun fetchNyanNyanConfig() {
        db.collection(configCollectionName)
            .get()
            .addOnSuccessListener { allData ->
                val nekosanPointMultiplier = allData.first { it.id == "np_multiplier" }["v"].toString().toInt()

                val ranksMasterDocumentName = if (Locale.getDefault().language == "ja") "np_rank_ja" else "np_rank_en"
                val ranks = HashMap<Int, NyanNyanRank>()
                allData.first { it.id == ranksMasterDocumentName }
                    .data.forEach {
                        val key = it.key.toInt()
                        val value = NyanNyanRank(
                            name = (it.value as Map<*, *>)["nam"].toString(),
                            point = (it.value as Map<*, *>)["pt"].toString().toInt()
                        )
                        ranks[key] = value
                    }
                _nyanNyanConfigEvent.postValue(NyanNyanConfig(nekosanPointMultiplier, ranks))
            }
    }

    private fun createNyanNyanUser(user: TwitterUserRecord) {
        val userDocument = hashMapOf("np" to 0, "tc" to 0)
        db.collection(usersCollectionName)
            .document(user.sealedUserId)
            .set(userDocument)
            .addOnCompleteListener {
                println("nyaoo--n ${NyanNyanUser(id = user.sealedUserId, nekosanPoint = 0, tweetCount = 0)}")
                _nyanNyanUserEvent.postValue(
                    NyanNyanUser(id = user.sealedUserId, nekosanPoint = 0, tweetCount = 0)
                )
            }
    }
}
