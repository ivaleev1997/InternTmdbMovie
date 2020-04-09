package com.education.details.presentation

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.makeGone
import com.education.core_api.extension.makeVisible
import com.education.core_api.extension.observe
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.uievent.NoNetworkEvent
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.details.R
import com.education.details.di.DetailsComponent
import com.education.details.domain.entity.DetailsViewState
import com.education.details.domain.entity.LoadStatus
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.details_main_content.*
import javax.inject.Inject

class DetailsFragment : BaseFragment(R.layout.details_fragment) {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var favoriteItem: MenuItem

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactory
    }

    private val args: DetailsFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        DetailsComponent.create((requireActivity().application as AppWithComponent).getComponent()).inject(this)
        super.onAttach(context)
    }

    override fun initViewElements(view: View) {
        setupToolBar()
        viewModel.loadDetails(args.movieId, resources.getString(R.string.ru_locale_min))
        toolbar.setNavigationOnClickListener {
            navigateUp()
        }

        observeLiveDataChanges()
    }

    private fun observeLiveDataChanges() {
        observe(viewModel.liveState, ::renderView)
        observe(viewModel.favoriteState, ::changeFavoriteIcon)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        if (event is NoNetworkEvent && !detailsMainContent.isVisible) {
            loadAnimation.makeGone()
            networkErrorView.makeVisible()
        } else
            onFragmentEvent(event, detailsFragment)
    }

    private fun setupToolBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).title = " "
        setHasOptionsMenu(true)

    }

    private fun renderView(detailsViewState: DetailsViewState) {
        when (detailsViewState.loadStatus) {
            LoadStatus.SUCCESS -> {
                if (detailsViewState.movieOverView != null)
                    with(detailsViewState.movieOverView) {
                        Glide.with(requireContext())
                            .load(posterPath)
                            .placeholder(resources.getDrawable(R.drawable.image_placeholder))
                            .transform(CenterCrop(), RoundedCorners(8))
                            .into(posterImageView)

                        titleTextView.text = title
                        originalTitleTextView.text = originalTitle
                        genresTextView.text = genre
                        runTimeTextView.text = runTime
                        voteAverageTextView.text = voteAverage
                        voteCountTextView.text = voteCount
                        changeFavoriteIcon(isFavorite)
                        if (overView.isBlank()) {
                            noContentView.visibility = View.VISIBLE
                        } else {
                            overViewContainer.visibility = View.VISIBLE
                            overViewTextView.text = overView
                        }
                    }
                loadAnimation.makeGone()
                detailsMainContent.makeVisible()
            }

            LoadStatus.LOAD -> {
                loadAnimation.makeVisible()
            }
        }
    }

    private fun changeFavoriteIcon(flag: Boolean?) {
        if (flag != null) {
            favoriteItem.icon =
                if (flag)
                    resources.getDrawable(R.drawable.ic_favorite_clicked)
                else
                    resources.getDrawable(R.drawable.ic_favorite_not_clicked)

            favoriteItem.isVisible = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite, menu)
        favoriteItem = menu.findItem(R.id.favorite)
        favoriteItem.setIcon(R.drawable.ic_favorite_not_clicked)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> viewModel.onFavoriteClicked()
        }

        return true
    }
}
