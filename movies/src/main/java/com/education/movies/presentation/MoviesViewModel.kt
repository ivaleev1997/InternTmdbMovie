package com.education.movies.presentation

import androidx.lifecycle.MutableLiveData
import com.education.core_api.RX_DEBOUNCE_INTERVAL
import com.education.core_api.data.network.exception.UnAuthorizedException
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.delegate
import com.education.core_api.extension.isNetworkException
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.presentation.uievent.AnotherEvent
import com.education.core_api.presentation.uievent.NoNetworkEvent
import com.education.core_api.presentation.uievent.UnAuthorizedEvent
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

    private fun createInitialState() = MoviesViewState(listOf(), MoviesListState.CLEAN)

    private var state: MoviesViewState by liveState.delegate()

    fun initSearchMovies(observableQuery: Flowable<String>) {
        observableQuery
            .skip(1)
            .observeOn(schedulersProvider.mainThread())
            .doOnEach {
                setState(listOf(), MoviesListState.ON_SEARCH)
            }
            .observeOn(schedulersProvider.computation())
            .debounce(RX_DEBOUNCE_INTERVAL, TimeUnit.MILLISECONDS)
            .map { query -> query.toLowerCase(Locale.getDefault()).trim() }
            .observeOn(schedulersProvider.io())
            .switchMapSingle { query -> moviesUseCase.searchQuery(query) }
            .schedulersComputationToMain(schedulersProvider)
            .doOnError { error -> handleError(error) }
            .retry()
            .subscribe(
                { queryAndMovies ->
                    when {
                        queryAndMovies.first.isBlank() -> {
                            setState(queryAndMovies.second, MoviesListState.CLEAN)
                        }
                        queryAndMovies.first.isNotBlank() && queryAndMovies.second.isEmpty() -> {
                            setState(queryAndMovies.second, MoviesListState.EMPTY)
                        }
                        else -> {
                            setState(queryAndMovies.second, MoviesListState.NONE_EMPTY)
                        }
                    }
                },
                { error -> Timber.e(error) }
            ).autoDispose()
    }

    private fun handleError(error: Throwable) {
        when {
            error.isNetworkException() -> sendEvent(
                object : NoNetworkEvent {}
            )

            error is UnAuthorizedException -> sendEvent(
                object : UnAuthorizedEvent {}
            )

            else -> sendEvent(
                object : AnotherEvent {}
            )
        }
        Timber.e(error)
    }

    private fun setState(moviesList: List<Movie>, moviesListState: MoviesListState) {
        state = state.copy(movies = moviesList, moviesListState = moviesListState)
    }

    fun onClearTextIconClick() {
        state = createInitialState()
    }
}
