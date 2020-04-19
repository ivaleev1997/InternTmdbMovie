package com.education.core_impl.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.education.core_api.data.local.AppDb
import com.education.core_api.data.local.entuty.Movie

@Database(
    entities = [Movie::class],
    version = 3,
    exportSchema = true
)
abstract class AppDbImpl : RoomDatabase(), AppDb