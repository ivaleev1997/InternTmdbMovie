package com.education.details

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        observe(viewModel.liveState, ::renderView)

        toolbar.setNavigationOnClickListener {
            navigateUp()
        }
    }

    private fun setupToolBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).title = " "
        setHasOptionsMenu(true)

    }

    private fun renderView(detailsViewState: DetailsViewState) {
        when (detailsViewState.loadStatus) {
            LoadStatus.SUCCESS -> {
                with(detailsViewState.movieOverView.first()) {
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
                        emptyAnimation.visibility = View.VISIBLE
                    } else {
                        overViewContainer.visibility = View.VISIBLE
                        overViewTextView.text = overView
                    }
                }
            }

            LoadStatus.FAVORITE -> {
                changeFavoriteIcon(detailsViewState.movieOverView.first().isFavorite)
            }
            LoadStatus.LOAD -> {
            }
        }
    }

    private fun changeFavoriteIcon(flag: Boolean) {
        if (flag)
            favoriteItem.setIcon(R.drawable.ic_favorite_clicked)
        else
            favoriteItem.setIcon(R.drawable.ic_favorite_not_clicked)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite, menu)
        favoriteItem = menu.findItem(R.id.favorite)
        favoriteItem.setIcon(R.drawable.ic_favorite_not_clicked)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                viewModel.onFavoriteClicked()
            }
        }

        return true
    }
}
