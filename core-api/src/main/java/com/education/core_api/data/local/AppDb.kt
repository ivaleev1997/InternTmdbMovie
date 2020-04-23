package com.education.core_api.data.local

import com.education.core_api.data.local.dao.MovieDao

interface AppDb {
    companion object {
        const val APP_DB_TABLE_MOVIE = "Movie"
    }

    fun getMovieDao(): MovieDao
}
