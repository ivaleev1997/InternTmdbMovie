package com.education.movies.presentation

import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.extension.schedulersIoToMain
import com.education.movies.domain.MoviesUseCase
import com.education.search.domain.entity.MoviesScreenState
import com.education.search.presentation.MoviesRecyclerViewModel
import io.reactivex.Flowable
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class MoviesViewModel(
    private val moviesUseCase: MoviesUseCase,
    private val schedulersProvider: SchedulersProvider
) : MoviesRecyclerViewModel() {

    companion object {
        const val RX_DEBOUNCE_INTERVAL_500 = 500L // mills
    }

    init {
        loadRecyclerMapState()
    }

    override fun initSearchMovies(observableQuery: Flowable<String>) {
        observableQuery
            .distinctUntilChanged()
            .skip(1)
            .observeOn(schedulersProvider.mainThread())
            .filter { query -> query != lastFetchedQuery }
            .doOnEach {
                setMoviesScreenState(MoviesScreenState.ON_SEARCH, moviesToRecyclerItem(currentListMovies))
            }
            .observeOn(schedulersProvider.computation())
            .debounce(RX_DEBOUNCE_INTERVAL_500, TimeUnit.MILLISECONDS)
            .map { query -> query.toLowerCase(Locale.getDefault()).trim() }
            .observeOn(schedulersProvider.io())
            .switchMapSingle { query -> moviesUseCase.searchQuery(query) }
            .schedulersComputationToMain(schedulersProvider)
            .doOnError { error -> handleError(error) }
            .retry()
            .subscribe(
                { (query, movies) ->
                    lastFetchedQuery = query
                    handleQueryAndMovies(query, movies)
                },
                { error -> Timber.e(error) }
            ).autoDispose()
    }

    override fun saveRecyclerMapState(isLinearLayout: Boolean) {
        moviesUseCase.saveRecyclerMapState(isLinearLayout)
            .schedulersIoToMain(schedulersProvider)
            .subscribe()
            .autoDispose()
    }

    override fun loadRecyclerMapState() {
        moviesUseCase.getRecyclerMapState()
            .schedulersIoToMain(schedulersProvider)
            .subscribe(::reMapRecycler) { error ->
                Timber.d(error)
            }
            .autoDispose()
    }
}
