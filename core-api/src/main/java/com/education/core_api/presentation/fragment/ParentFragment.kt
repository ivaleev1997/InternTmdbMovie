package com.education.core_api.presentation.fragment

import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class ParentFragment : NavHostFragment() {

    abstract fun getBottomNavigationView(): BottomNavigationView
}