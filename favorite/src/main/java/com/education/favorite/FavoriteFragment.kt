package com.education.favorite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.education.core_api.presentation.fragment.BaseFragment

class FavoriteFragment : BaseFragment(R.layout.favorite_fragment) {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var viewModel: FavoriteViewModel

    override fun initViewElements(view: View) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
