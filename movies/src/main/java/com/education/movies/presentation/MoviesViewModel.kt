package com.education.movies.presentation

import androidx.lifecycle.MutableLiveData
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.delegate
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.movies.domain.MoviesUseCase
import com.education.movies.domain.entity.MoviesListState
import com.education.movies.domain.entity.MoviesViewState
import com.education.search.entity.Movie
import io.reactivex.Flowable
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class MoviesViewModel(
    private val moviesUseCase: MoviesUseCase,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    val liveState = MutableLiveData(createInitialState())

    private fun createInitialState() = MoviesViewState(listOf(), MoviesListState.CREATED)

    private var state: MoviesViewState by liveState.delegate()

    fun initSearchMovies(observableQuery: Flowable<String>) {
        observableQuery
            .debounce(500, TimeUnit.MILLISECONDS)
            .map { query -> query.toLowerCase(Locale.getDefault()).trim() }
            .observeOn(schedulersProvider.io())
            .switchMap { query -> moviesUseCase.searchQuery(query) }
            .schedulersComputationToMain(schedulersProvider)
            .subscribe (
                { queryAndMovies ->
                    when {
                        queryAndMovies.first.isBlank() -> {
                            setState(queryAndMovies.second, MoviesListState.CREATED)
                        }
                        queryAndMovies.first.isNotBlank() && queryAndMovies.second.isEmpty() -> {
                            setState(queryAndMovies.second, MoviesListState.EMPTY)
                        }
                        else -> {
                            setState(queryAndMovies.second, MoviesListState.NONE_EMPTY)
                        }
                    }
                },
                { t ->
                    Timber.e(t)
                }
            ).autoDispose()
    }

    private fun setState(moviesList: List<Movie>, moviesListState: MoviesListState) {
        state = state.copy(movies = moviesList, moviesListState = moviesListState)
    }

    fun onClearTextIconClick() {
        state = createInitialState()
    }
}
