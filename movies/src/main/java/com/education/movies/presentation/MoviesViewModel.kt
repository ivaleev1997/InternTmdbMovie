package com.education.movies.presentation

import androidx.lifecycle.MutableLiveData
import com.education.core_api.RX_DEBOUNCE_INTERVAL_500
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.delegate
import com.education.core_api.extension.mapDistinct
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.presentation.uievent.NavigateToEvent
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.movies.domain.MoviesUseCase
import com.education.movies.domain.entity.MoviesScreenState
import com.education.movies.domain.entity.MoviesViewState
import com.education.search.domain.entity.Movie
import com.education.search.presentation.recycleritem.MovieListItem
import com.education.search.presentation.recycleritem.MovieTileItem
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.Flowable
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class MoviesViewModel(
    private val moviesUseCase: MoviesUseCase,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {
    private var isListRecyclerMap = true
    private var currentListMovies = listOf<Movie>()
    private var lastFetchedQuery: String = ""

    val liveState = MutableLiveData(createInitialState())
    private var state: MoviesViewState by liveState.delegate()

    val moviesScreenState = liveState.mapDistinct { it.moviesScreenState }
    val recyclerMapState = liveState.mapDistinct { it.isLineRecyclerMap }
    val adapterItemsState = liveState.mapDistinct { it.listItems }

    fun initSearchMovies(observableQuery: Flowable<String>) {
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
                { queryAndMovies ->
                    lastFetchedQuery = queryAndMovies.first
                    when {
                        queryAndMovies.first.isBlank() -> {
                            setMoviesScreenState(MoviesScreenState.CLEAN, moviesToRecyclerItem(queryAndMovies.second))
                        }
                        queryAndMovies.first.isNotBlank() && queryAndMovies.second.isEmpty() -> {
                            moviesToRecyclerItem(queryAndMovies.second)
                            setMoviesScreenState(MoviesScreenState.EMPTY, moviesToRecyclerItem(queryAndMovies.second))
                        }
                        else -> {
                            moviesToRecyclerItem(queryAndMovies.second)
                            setMoviesScreenState(MoviesScreenState.NONE_EMPTY, moviesToRecyclerItem(queryAndMovies.second))
                        }
                    }
                },
                {
                        error -> Timber.e(error)
                }
            ).autoDispose()
    }

    fun onClearTextIconClick() {
        state = createInitialState()
    }

    fun onReMapRecyclerClick() {
        isListRecyclerMap = !isListRecyclerMap
        setAdapterItems(moviesToRecyclerItem(currentListMovies))
        setRecyclerMapState(isListRecyclerMap)
    }

    private fun createInitialState() = MoviesViewState(
        MoviesScreenState.CLEAN,
        listOf(),
        isListRecyclerMap
    )

    private fun setMoviesScreenState(moviesScreenState: MoviesScreenState, listItems: List<Item>) {
        state = state.copy(moviesScreenState = moviesScreenState, listItems = listItems)
    }

    private fun setRecyclerMapState(mapStateFlag: Boolean) {
        state = state.copy(isLineRecyclerMap = mapStateFlag)
    }

    private fun setAdapterItems(listItems: List<Item>) {
        state = state.copy(listItems = listItems)
    }

    private fun moviesToRecyclerItem(moviesList: List<Movie>): List<Item> {
        currentListMovies = moviesList
        return moviesList.map { movie ->
            if (isListRecyclerMap) {
                MovieListItem(
                    movie,
                    ::navigateToDetails
                )
            } else {
                MovieTileItem(
                    movie,
                    ::navigateToDetails
                )
            }
        }
    }

    private fun navigateToDetails(movie: Movie) {
        sendEvent(NavigateToEvent(
            MoviesFragmentDirections.actionFilmsToDetails(movie.id)
        ))
    }
}
