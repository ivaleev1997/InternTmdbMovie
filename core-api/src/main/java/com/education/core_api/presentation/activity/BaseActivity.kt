package com.education.core_api.presentation.activity

import androidx.navigation.NavDirections

interface BaseActivity {

    fun startMainAppScreen()

    fun startLoginScreen()

    fun logout()

    fun navigateTo(navDirection: NavDirections)
}
