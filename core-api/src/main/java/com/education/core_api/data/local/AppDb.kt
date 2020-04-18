package com.education.core_api.data.local

import com.education.core_api.data.local.dao.MovieDao

interface AppDb {
    fun getMovieDao(): MovieDao
}