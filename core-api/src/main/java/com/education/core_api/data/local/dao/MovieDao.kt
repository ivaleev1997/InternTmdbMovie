package com.education.core_api.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.education.core_api.data.local.entuty.Movie
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getMovies(): Single<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>): Completable
}