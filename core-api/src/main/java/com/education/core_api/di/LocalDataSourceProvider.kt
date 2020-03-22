package com.education.core_api.di

import com.education.core_api.data.LocalDataSource

interface LocalDataSourceProvider {

    fun provideLocalDataSource(): LocalDataSource
}