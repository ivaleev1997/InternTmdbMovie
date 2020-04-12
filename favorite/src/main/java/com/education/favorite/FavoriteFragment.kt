package com.education.favorite

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.makeGone
import com.education.core_api.extension.makeVisible
import com.education.core_api.extension.observe
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.uievent.NoNetworkEvent
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.favorite.di.FavoriteComponent
import com.education.favorite.domain.entity.LoadFavoriteStatus
import com.education.favorite.domain.entity.LoadFavoritesViewState
import com.education.search.presentation.RecyclerFragment
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.BackpressureStrategy
import kotlinx.android.synthetic.main.favorite_fragment.*
import timber.log.Timber
import javax.inject.Inject

class FavoriteFragment : RecyclerFragment(R.layout.favorite_fragment) {

    companion object {
        fun newInstance() = FavoriteFragment()
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

        observeLiveDataChanges()

    }

    private fun setupSearchInput() {
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
        observe(viewModel.loadLiveState, ::renderLoadFavorites)
        observe(viewModel.recyclerMapState, ::renderRecyclerMapState)
        observe(viewModel.adapterItemsState, ::updateAdapter)
        observe(viewModel.eventsQueue, ::onEvent)
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

            // Установка курсора на searchInputLayout.editText
            searchInputLayout.editText?.isFocusableInTouchMode = true
            searchInputLayout.editText?.requestFocus()

            // Показать клавиатуру keyboard
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(searchInputLayout.editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun renderLoadFavorites(loadFavoritesViewState: LoadFavoritesViewState) {
        Timber.d("loadFavorites: ${loadFavoritesViewState.loadFavoriteStatus}")
        when (loadFavoritesViewState.loadFavoriteStatus) {
            LoadFavoriteStatus.LOAD -> {
                loadAnimation.makeVisible()
            }
            LoadFavoriteStatus.NON_EMPTY -> {
                // visible recycler
                makeRecyclerVisible()
                loadAnimation.makeGone()
                mainContent.makeVisible()
            }
            LoadFavoriteStatus.EMPTY -> {
                makeRecyclerGone()
                loadAnimation.makeGone()
                emptyContent.makeVisible()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }
}
