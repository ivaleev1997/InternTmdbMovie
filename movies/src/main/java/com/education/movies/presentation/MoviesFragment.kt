package com.education.movies.presentation

import android.content.Context
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.observe
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.movies.R
import com.education.movies.di.MoviesComponent
import com.education.movies.domain.entity.MoviesListState
import com.education.movies.domain.entity.MoviesViewState
import com.education.search.MovieAdapter
import com.education.search.entity.Movie
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.BackpressureStrategy
import kotlinx.android.synthetic.main.movies_fragment.*
import timber.log.Timber
import javax.inject.Inject

class MoviesFragment : BaseFragment(R.layout.movies_fragment) {

    companion object {
        fun newInstance() = MoviesFragment()
    }

    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var adapter: MovieAdapter

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
            loginTextEdit.textChanges()
                .map { it.toString() }
                .toFlowable(
                    BackpressureStrategy.LATEST
                )
        )

        searchInputLayout.setEndIconOnClickListener {
            searchInputLayout.editText?.setText("")
            viewModel.onClearTextIconClick()
            Timber.d("changeVisibilityForView for progress bar")
            changeVisibilityForView(progressBar, View.GONE)
        }

        moviesRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MovieAdapter(
            resources.getColor(R.color.green_color),
            resources.getString(R.string.ru_locale_min)
        ) { movie ->
            navigateTo(MoviesFragmentDirections.actionFilmsToDetails(movie.title))
        }

        moviesRecyclerView.adapter = adapter

        observe(viewModel.liveState, ::renderView)
        observe(viewModel.eventsQueue, ::onEvent)

        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                hideKeyboard()
            }
        })

        moviesConstrainContainer.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun onEvent(event: Event) {
        onFragmentEvent(event, moviesConstrainContainer)
    }

    private fun renderView(moviesViewState: MoviesViewState) {
        when (moviesViewState.moviesListState) {
            MoviesListState.EMPTY -> {
                Timber.d("Visible EMPTY")
                renderNotFoundScreen()
            }
            MoviesListState.NONE_EMPTY -> {
                Timber.d("Visible NONE_EMPTY")
                renderRecyclerView(moviesViewState.movies)
            }
            MoviesListState.CLEAN -> {
                Timber.d("Visible CLEAN")
                cleanState()
            }
            MoviesListState.ON_SEARCH -> {
                Timber.d("Visible ON_SEARCH")
                //progressBar.visibility = View.VISIBLE
                changeVisibilityForView(progressBar, View.VISIBLE)
            }
        }
    }

    private fun renderRecyclerView(moviesList: List<Movie>) {
        adapter.listMovies = moviesList

        changeVisibilityForView(progressBar, View.GONE)

        changeVisibilityForView(notfoundImageView, View.GONE)

        changeVisibilityForView(notfoundTextView, View.GONE)

        changeVisibilityForView(moviesRecyclerView, View.VISIBLE)
    }

    private fun changeVisibilityForView(view: View, visibleFlag: Int) {
        when (visibleFlag) {
            View.GONE -> {
                if (!view.isGone)
                    view.visibility = View.GONE
            }
            View.VISIBLE -> {
                if (!view.isVisible)
                    view.visibility = View.VISIBLE
            }
        }
    }

    private fun renderNotFoundScreen() {
        changeVisibilityForView(progressBar, View.GONE)

        changeVisibilityForView(moviesRecyclerView, View.GONE)

        changeVisibilityForView(notfoundImageView, View.VISIBLE)

        changeVisibilityForView(notfoundTextView, View.VISIBLE)
    }

    private fun cleanState() {
        changeVisibilityForView(progressBar, View.GONE)

        changeVisibilityForView(moviesRecyclerView, View.GONE)

        changeVisibilityForView(notfoundImageView, View.GONE)

        changeVisibilityForView(notfoundTextView, View.GONE)
    }
}
