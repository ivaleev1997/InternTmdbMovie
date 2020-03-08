package com.education.core_api.ui

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface LoginMediator {
    fun startLoginScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}