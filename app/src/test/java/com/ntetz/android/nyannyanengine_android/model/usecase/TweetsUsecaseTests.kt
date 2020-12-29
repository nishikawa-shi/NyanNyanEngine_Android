package com.ntetz.android.nyannyanengine_android.model.usecase

import com.google.common.truth.Truth
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.User
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.repository.IAccountRepository
import com.ntetz.android.nyannyanengine_android.model.repository.ITweetsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TweetsUsecaseTests {
    @Mock
    private lateinit var mockAccountRepository: IAccountRepository

    @Mock
    private lateinit var mockTweetsRepository: ITweetsRepository

    @Test
    fun getTweets_認証情報がない時未ログインの値を返すこと() = runBlocking {
        `when`(mockAccountRepository.loadTwitterUser(this)).thenReturn(null)

        val testUsecase = TweetsUsecase(mockTweetsRepository, mockAccountRepository)
        Truth.assertThat(testUsecase.getTweets(this)).isEqualTo(
            listOf(
                Tweet(
                    id = 28,
                    text = "左上のボタンからログインするにゃあ♪\nあなたのTwitterをネコ語化だにゃ",
                    createdAt = "2828/2/8",
                    user = User(
                        name = "にゃんにゃ先生",
                        screenName = "NNyansu",
                        profileImageUrlHttps = "https://nyannyanengine.firebaseapp.com/assets/nyannya_sensei.png"
                    )
                )
            )
        )
    }

    @Test
    fun getTweets_レポジトリ由来の値を返すこと() = runBlocking {
        withContext(Dispatchers.IO) {
            val testUser = TwitterUserRecord(
                id = "getTweetsResChId",
                oauthToken = "getTweetsResChToken",
                oauthTokenSecret = "getTweetsResChSecret",
                screenName = "getTweetsResChSNm"
            )
            `when`(mockAccountRepository.loadTwitterUser(this)).thenReturn(
                testUser
            )
            `when`(mockTweetsRepository.getTweets(user = testUser, scope = this)).thenReturn(
                listOf(
                    Tweet(
                        id = 2828,
                        text = "dummyUsCsNom",
                        createdAt = "3 gatsu 2 nichi",
                        user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
                    )
                )
            )

            val testUsecase = TweetsUsecase(mockTweetsRepository, mockAccountRepository)
            Truth.assertThat(testUsecase.getTweets(this)).isEqualTo(
                listOf(
                    Tweet(
                        id = 2828,
                        text = "dummyUsCsNom",
                        createdAt = "3 gatsu 2 nichi",
                        user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
                    )
                )
            )
        }
    }

    @Test
    fun getLatestTweets_レポジトリ由来の値を返すこと() = runBlocking {
        withContext(Dispatchers.IO) {
            val testUser = TwitterUserRecord(
                id = "getTweetsResChId",
                oauthToken = "getTweetsResChToken",
                oauthTokenSecret = "getTweetsResChSecret",
                screenName = "getTweetsResChSNm"
            )
            `when`(mockAccountRepository.loadTwitterUser(this)).thenReturn(
                testUser
            )
            `when`(mockTweetsRepository.getLatestTweets(user = testUser, scope = this)).thenReturn(
                listOf(
                    Tweet(
                        id = 2828,
                        text = "dummyUsCsNom",
                        createdAt = "3 gatsu 2 nichi",
                        user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
                    )
                )
            )

            val testUsecase = TweetsUsecase(mockTweetsRepository, mockAccountRepository)
            Truth.assertThat(testUsecase.getLatestTweets(this)).isEqualTo(
                listOf(
                    Tweet(
                        id = 2828,
                        text = "dummyUsCsNom",
                        createdAt = "3 gatsu 2 nichi",
                        user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
                    )
                )
            )
        }
    }

    @Test
    fun getPreviousTweets_レポジトリ由来の値を返すこと() = runBlocking {
        withContext(Dispatchers.IO) {
            val testUser = TwitterUserRecord(
                id = "getTweetsResChId",
                oauthToken = "getTweetsResChToken",
                oauthTokenSecret = "getTweetsResChSecret",
                screenName = "getTweetsResChSNm"
            )
            `when`(mockAccountRepository.loadTwitterUser(this)).thenReturn(
                testUser
            )
            `when`(
                mockTweetsRepository.getPreviousTweets(
                    maxId = 1234567890123456789,
                    user = testUser,
                    scope = this
                )
            ).thenReturn(
                listOf(
                    Tweet(
                        id = 2828,
                        text = "dummyUsCsNomPrev",
                        createdAt = "3 gatsu 2 nichi",
                        user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
                    )
                )
            )

            val testUsecase = TweetsUsecase(mockTweetsRepository, mockAccountRepository)
            Truth.assertThat(testUsecase.getPreviousTweets(maxId = 1234567890123456789, this)).isEqualTo(
                listOf(
                    Tweet(
                        id = 2828,
                        text = "dummyUsCsNomPrev",
                        createdAt = "3 gatsu 2 nichi",
                        user = User("dummyUsCsNomName", "dummyUsCsNomScNm", "https://ntetz.com/dummyUsCsNom.jpg")
                    )
                )
            )
        }
    }
}
