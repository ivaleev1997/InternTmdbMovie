package com.education.details

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.education.core_api.presentation.fragment.BaseFragment
import kotlinx.android.synthetic.main.details_fragment.*

class DetailsFragment : BaseFragment(R.layout.details_fragment) {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private lateinit var viewModel: DetailsViewModel

    override fun initViewElements(view: View) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        val movieTitle = arguments?.getString("movieTitle")
        MovieTitle.text = movieTitle
    }
}
