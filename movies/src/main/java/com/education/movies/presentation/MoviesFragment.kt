package com.education.movies.presentation

import android.content.Context
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
import com.education.movies.R
import com.education.movies.di.MoviesComponent
import com.education.search.domain.entity.MoviesScreenState
import com.education.search.presentation.RecyclerFragment
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.BackpressureStrategy
import kotlinx.android.synthetic.main.movies_fragment.*
import timber.log.Timber
import javax.inject.Inject

class MoviesFragment : RecyclerFragment(R.layout.movies_fragment) {

    companion object {
        fun newInstance() = MoviesFragment()
    }

    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoviesViewModel by viewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        MoviesComponent.create((requireActivity().application as AppWithComponent).getComponent())
            .inject(this)
        super.onAttach(context)
    }

    override fun initViewElements(view: View) {
        viewModel.initSearchMovies(
            searchTextEdit.textChanges()
                .map { it.toString() }
                .toFlowable(
                    BackpressureStrategy.LATEST
                )
        )

        setupMoviesRecycler(viewModel.recyclerMapState.value == true)

        setupOnScrollRecyclerHideKeyboard()

        searchInputEndIconListener()

        moviesConstrainContainer.setOnClickListener {
            hideKeyboard()
        }

        setRecyclerChangeMapListener()

        setOnErrorRepeatListener()

        observeLiveDataChanges()

    }

    private fun searchInputEndIconListener() {
        searchInputLayout.setEndIconOnClickListener {
            searchInputLayout.editText?.setText("")
            viewModel.onClearTextIconClick()
            progressBar.makeGone()
        }
    }

    private fun observeLiveDataChanges() {
        observe(viewModel.moviesScreenState, ::renderView)
        observe(viewModel.recyclerMapState, ::renderRecyclerMapState)
        observe(viewModel.adapterItemsState, ::updateAdapter)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun setOnErrorRepeatListener() {
        networkErrorView.setOnRepeatClickListener {
            networkErrorView.makeGone()
            mainContent.makeVisible()
            // TODO retry request
            //val currentQuery = "${loginTextEdit.text} "
            //loginTextEdit.setText(currentQuery)
            //loginTextEdit.setText(loginTextEdit.text?.trim())
        }
    }

    private fun onEvent(event: Event) {
        Timber.d("onEvent")
        if (event is NoNetworkEvent) {
            fromZeroToCleanState()
            progressBar.makeGone()
            mainContent.makeGone()
            cleanState()
            networkErrorView.makeVisible()
            hideKeyboard()
        } else {
            fromZeroToCleanState()
            onFragmentEvent(event, moviesConstrainContainer)
        }
    }

    private fun renderView(moviesScreenState: MoviesScreenState) {
        when (moviesScreenState) {
            MoviesScreenState.EMPTY -> {
                Timber.d("EMPTY")

                fromZeroToCleanState()
                renderNotFoundScreen()
            }
            MoviesScreenState.NONE_EMPTY -> {
                Timber.d("NONE_EMPTY")

                fromZeroToCleanState()
                renderRecyclerView()
            }
            MoviesScreenState.CLEAN -> {
                Timber.d("CLEAN")

                fromZeroToCleanState()
                cleanState()
            }
            MoviesScreenState.ON_SEARCH -> {
                Timber.d("ON_SEARCH")
                fromZeroToCleanState()
                progressBar.makeVisible()
            }
            MoviesScreenState.ZERO -> {
                Timber.d("ZERO")
                renderZeroState()
            }
        }
    }

    private fun renderZeroState() {
        mainContent.makeGone()
        progressBar.makeGone()
        moviesRecycler.makeGone()
        notfoundImage.makeGone()
        notfoundText.makeGone()

        stubSearchInput.makeVisible()
        stubSearchTextEdit.makeVisible()

        stubSearchTextEdit.setOnClickListener {
            fromZeroToCleanState()
            setFocusableEditText(searchInputLayout.editText)
            showKeyboard(searchInputLayout.editText)
        }
    }

    private fun fromZeroToCleanState() {
        stubSearchImageView.makeGone()
        stubSearchInput.makeGone()
        stubSearchTitleTextView.makeGone()

        cleanState()
    }

    private fun renderRecyclerView() {
        mainContent.makeVisible()
        progressBar.makeGone()
        notfoundImage.makeGone()
        notfoundText.makeGone()
        moviesRecycler.makeVisible()
    }

    private fun renderNotFoundScreen() {
        mainContent.makeVisible()
        progressBar.makeGone()
        moviesRecycler.makeGone()
        notfoundImage.makeVisible()
        notfoundText.makeVisible()
    }

    private fun cleanState() {
        searchInputLayout.makeVisible()
        recyclerMap.makeVisible()
        mainContent.makeVisible()
        progressBar.makeGone()
        moviesRecycler.makeGone()
        notfoundImage.makeGone()
        notfoundText.makeGone()
    }

    private fun setRecyclerChangeMapListener() {
        recyclerMap.setOnClickListener {
            viewModel.onReMapRecyclerClick()
        }
    }

    private fun renderRecyclerMapState(isLineLayoutManager: Boolean) {
        changeRecyclerMapIcon(isLineLayoutManager)
        moviesRecycler.changeLayoutManager(isLineLayoutManager)
    }

    private fun changeRecyclerMapIcon(isLineLayoutManager: Boolean) {
        if (!isLineLayoutManager)
            recyclerMap.setImageResource(R.drawable.ic_to_list_map)
        else
            recyclerMap.setImageResource(R.drawable.ic_to_tile_map)
    }
}
