package com.education.details

import android.content.Context
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.observe
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.details.di.DetailsComponent
import com.education.details.domain.entity.DetailsViewState
import com.education.details.domain.entity.LoadStatus
import kotlinx.android.synthetic.main.details_fragment.*
import javax.inject.Inject

class DetailsFragment : BaseFragment(R.layout.details_fragment) {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactory
    }

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onAttach(context: Context) {
        DetailsComponent.create((requireActivity().application as AppWithComponent).getComponent()).inject(this)
        super.onAttach(context)
    }

    override fun initViewElements(view: View) {
        viewModel.loadDetails(args.movieId)
        observe(viewModel.liveState, ::renderView)
    }

    private fun renderView(detailsViewState: DetailsViewState) {
        when (detailsViewState.loadStatus) {
            LoadStatus.SUCCESS -> {
                with(detailsViewState.movieOverView.first()) {
                    Glide.with(requireContext())
                        .load(posterPath)
                        .transform(CenterCrop(), RoundedCorners(8))
                        .into(posterImageView)

                    titleTextView.text = title
                    originalTitleTextView.text = originalTitle
                    genresTextView.text = genre
                    runTimeTextView.text = runTime
                    voteAverageTextView.text = voteAverage
                    voteCountTextView.text = voteCount
                    overViewTextView.text = overView
                }
            }
            LoadStatus.LOAD -> {

            }
        }
    }
}
