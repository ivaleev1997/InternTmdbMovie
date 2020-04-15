package com.education.redmadrobottmdb.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.education.core_api.presentation.fragment.ParentFragment
import com.education.redmadrobottmdb.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : ParentFragment() {

    companion object {
        fun newInstance() =
            MainFragment()
    }

    override fun getBottomNavigationView(): BottomNavigationView {
        return mainFragmentNavView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigationWithNavComponent()
    }

    private fun setupBottomNavigationWithNavComponent() {
        val hostFragment =
            childFragmentManager.findFragmentById(R.id.mainNavHostFragment) as? NavHostFragment

        hostFragment?.findNavController()?.let {
            mainFragmentNavView.setupWithNavController(it)
        }

    }
}
