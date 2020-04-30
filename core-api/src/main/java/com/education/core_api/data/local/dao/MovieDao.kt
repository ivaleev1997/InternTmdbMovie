package com.education.core_api.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.education.core_api.data.local.AppDb
import com.education.core_api.data.local.entity.Movie
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Query("SELECT * FROM ${AppDb.APP_DB_TABLE_MOVIE}")
    fun getMovies(): Single<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>): Completable

    @Query("DELETE FROM ${AppDb.APP_DB_TABLE_MOVIE} WHERE id = :movieId")
    fun deleteMovie(movieId: Long)
}
