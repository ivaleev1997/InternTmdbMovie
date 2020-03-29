package com.education.redmadrobottmdb.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.education.redmadrobottmdb.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : NavHostFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigationWithNavComponent()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun setupBottomNavigationWithNavComponent() {
        val hostFragment = childFragmentManager .findFragmentById(R.id.main_nav_host_fragment) as? NavHostFragment
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
/*        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_films, R.id.navigation_favorite, R.id.navigation_profile
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)

        mainFragmentNavView.setupWithNavController(navController)*/
        hostFragment?.findNavController()?.let {
            mainFragmentNavView.setupWithNavController(it)
            //setToolbar(it)
        }
    }
}
