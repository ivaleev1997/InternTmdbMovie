package com.education.favorite.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.makeGone
import com.education.core_api.extension.makeVisible
import com.education.core_api.extension.observe
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.uievent.NoNetworkEvent
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.favorite.R
import com.education.favorite.di.FavoriteComponent
import com.education.search.domain.entity.LoadFavoriteStatus
import com.education.search.domain.entity.MoviesScreenState
import com.education.search.presentation.RecyclerFragment
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.BackpressureStrategy
import javax.inject.Inject
import kotlinx.android.synthetic.main.favorite_fragment.*
import timber.log.Timber

class FavoriteFragment : RecyclerFragment(R.layout.favorite_fragment) {

    companion object {
        fun newInstance() =
            FavoriteFragment()
    }

    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FavoriteViewModel by viewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        FavoriteComponent.create((requireActivity().application as AppWithComponent).getComponent()).inject(this)
        super.onAttach(context)
    }


    override fun initViewElements(view: View) {
        viewModel.loadFavorites()

        setupSearchInput()

        setupMoviesRecycler(viewModel.recyclerMapState.value == true)

        setupOnScrollRecyclerHideKeyboard()

        setSearchIconListener()

        setRecyclerChangeMapListener(recyclerMap, viewModel)

        setOnErrorRepeatListener()

        observeLiveDataChanges()
    }

    override fun setOnErrorRepeatListener() {
        networkErrorView.setOnRepeatClickListener {
            viewModel.onErrorRepeatClicked()
        }
    }

    override fun setupSearchInput() {
        viewModel.initSearchMovies(
            searchTextEdit.textChanges()
            .map { it.toString() }
            .toFlowable(
                BackpressureStrategy.LATEST
            ))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (!searchTextEdit?.text.isNullOrBlank()) {
            searchIcon.makeGone()
            searchInputLayout.makeVisible()
        }
    }

    private fun observeLiveDataChanges() {
        observe(viewModel.loadFavoritesStatus, ::renderLoadFavorites)
        observe(viewModel.moviesRetryStatus, ::renderAfterErrorRetryState)
        observe(viewModel.recyclerMapState, ::renderRecyclerMapState)
        observe(viewModel.adapterItemsState, ::updateAdapter)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun renderAfterErrorRetryState(moviesScreenState: MoviesScreenState) {
        when (moviesScreenState) {
            MoviesScreenState.RETRY -> {
                Timber.d("RETRY")
                mainContent.makeVisible()
                networkErrorView.makeGone()
                loadAnimation.makeVisible()
                hideKeyboard()
            }
        }
    }

    private fun renderRecyclerMapState(flag: Boolean) {
        renderRecyclerMapState(recyclerMap, flag)
    }

    private fun onEvent(event: Event) {
        Timber.d("onEvent")
        loadAnimation.makeGone()
        if (event is NoNetworkEvent) {
            mainContent.makeGone()
            networkErrorView.makeVisible()
            hideKeyboard()
        } else
            onFragmentEvent(event, favoriteConstrainContainer)
    }

    private fun setSearchIconListener() {
        searchIcon.setOnClickListener {
            searchIcon.makeGone()
            searchInputLayout.makeVisible()

            setFocusableEditText(searchInputLayout.editText)
            showKeyboard(searchInputLayout.editText)
            }
    }

    private fun renderLoadFavorites(loadFavoritesViewState: LoadFavoriteStatus) {
        when (loadFavoritesViewState) {
            LoadFavoriteStatus.LOAD -> {
                Timber.d("LOAD")
                loadAnimation.makeVisible()
            }
            LoadFavoriteStatus.NON_EMPTY -> {
                Timber.d("NON_EMPTY")
                moviesRecycler.makeVisible()
                loadAnimation.makeGone()
                mainContent.makeVisible()
            }
            LoadFavoriteStatus.EMPTY -> {
                Timber.d("EMPTY")
                moviesRecycler.makeGone()
                loadAnimation.makeGone()
                emptyContent.makeVisible()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }
}
