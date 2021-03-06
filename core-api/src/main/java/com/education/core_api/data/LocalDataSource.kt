package com.education.core_api.data

import android.content.Context
import com.education.core_api.data.local.entity.Movie
import com.education.core_api.dto.UserCredentials
import io.reactivex.Completable
import io.reactivex.Single

interface LocalDataSource {

    fun setMasterPinKey(pin: String)

    fun saveMasterPinKey(pinKey: String): Boolean

    fun getMasterPinKey(): String

    fun saveSessionId(sessionId: String): Boolean

    fun saveRequestToken(token: String): Boolean

    fun saveTokenLifeTime(expiresAt: String): Boolean

    fun getSessionId(): String

    fun getRequestToken(): String

    fun getTokenLifeTime(): Long

    fun cleanTokens()

    fun saveUserLogin(userLogin: String): Boolean

    fun getUserLogin(): String

    fun saveUserPassword(userPass: String): Boolean

    fun getUserPassword(): String

    fun saveUserName(userName: String, context: Context): Boolean

    fun getUserName(): String

    fun saveUserCredentials(userCredentials: UserCredentials): Boolean

    fun saveOnStopTime(timeMills: Long): Boolean

    fun getOnStopTime(): Long

    fun clearLastOnStopTime()

    fun saveMovies(listMovies: List<Movie>): Completable

    fun getMovies(): Single<List<Movie>>

    fun saveRecyclerMapState(flag: Boolean)

    fun getRecyclerMapState(): Boolean

    fun deleteMovie(movieId: Long)
}
