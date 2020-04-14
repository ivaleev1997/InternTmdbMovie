package com.education.pin.data

import android.content.Context
import com.education.core_api.data.LocalDataSource
import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_api.data.network.exception.TryLaterException
import com.education.core_api.dto.UserCredentials
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PinRepositoryImpl @Inject constructor(
    private val tmdbMovieApi: TmdbMovieApi,
    private val localDataSource: LocalDataSource
): PinRepository {
    override fun saveCredentials(userCredentials: UserCredentials, pin: String): Completable {
        return Completable.fromAction {
            localDataSource.setMasterKey(pin)
            if (!localDataSource.saveUserCredentials(userCredentials))
                throw TryLaterException("Error while save user credentials")
        }
    }

    override fun fetchAndSaveUserName(context: Context): Completable {
        return tmdbMovieApi.getAccountInfo()
            .flatMapCompletable { account ->
                Completable.fromAction {
                    val name =
                        if (account.name.isEmpty())
                            account.username
                        else
                            account.name
                    if (!localDataSource.saveUserName(name, context))
                        throw TryLaterException("Error while save username")
                }
            }
    }

    override fun getUserName(): Single<String> {
        return Single.just(
            localDataSource.getUserName()
        )
    }

    override fun getUserLogin(pin: String): Single<String> {
        localDataSource.setMasterKey(pin)

        return Single.just(
            localDataSource.getUserLogin()
        )
    }
}