package com.education.search.presentation

import androidx.lifecycle.MutableLiveData
import com.education.core_api.extension.delegate
import com.education.core_api.extension.mapDistinct
import com.education.core_api.presentation.uievent.NavigateToEvent
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.movies.presentation.MoviesFragmentDirections
import com.education.search.domain.entity.DomainMovie
import com.education.search.domain.entity.MoviesScreenState
import com.education.search.domain.entity.MoviesViewState
import com.education.search.presentation.recycleritem.MovieListItem
import com.education.search.presentation.recycleritem.MovieTileItem
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.Flowable
import timber.log.Timber

abstract class MoviesRecyclerViewModel : BaseViewModel() {
    protected var currentListMovies = listOf<DomainMovie>()
    protected var lastFetchedQuery: String = ""

    val liveState = MutableLiveData(createInitialMoviesViewState())
    protected var state: MoviesViewState by liveState.delegate()

    val moviesScreenState = liveState.mapDistinct { it.moviesScreenState }
    val recyclerMapState = liveState.mapDistinct { it.isLinearLayoutRecyclerMap }
    val adapterItemsState = liveState.mapDistinct { it.listItems }

    abstract fun initSearchMovies(observableQuery: Flowable<String>)

    fun onClearTextIconClick() {
        state = createInitialMoviesViewState()
    }

    fun onReMapRecyclerClick() {
        val isLinearRecyclerMap = !(recyclerMapState.value as Boolean)
        setRecyclerMapState(isLinearRecyclerMap)
        setAdapterItems(moviesToRecyclerItem(currentListMovies))
    }

    protected fun handleQueryAndMovies(movies: Pair<String, List<DomainMovie>>) {
        Timber.d("handleQueryAndMovies: (${movies.first};${movies.second})")
        when {
            movies.first.isBlank() -> {
                setMoviesScreenState(MoviesScreenState.CLEAN, moviesToRecyclerItem(movies.second))
            }
            movies.first.isNotBlank() && movies.second.isEmpty() -> {
                setMoviesScreenState(MoviesScreenState.EMPTY, moviesToRecyclerItem(movies.second))
            }
            else -> {
                setMoviesScreenState(MoviesScreenState.NONE_EMPTY, moviesToRecyclerItem(movies.second))
            }
        }
    }

    protected fun moviesToRecyclerItem(moviesList: List<DomainMovie>): List<Item> {
        currentListMovies = moviesList
        val isLinearRecyclerMap = recyclerMapState.value as Boolean
        return moviesList.map { movie ->
            if (isLinearRecyclerMap) {
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

    private fun createInitialMoviesViewState() =
        MoviesViewState(
            MoviesScreenState.CLEAN,
            listOf()
        )

    protected fun setMoviesToScreenState(listDomainMovies: List<DomainMovie>) {
        state = state.copy(listItems = moviesToRecyclerItem(listDomainMovies))
    }

    protected fun setMoviesScreenState(moviesScreenState: MoviesScreenState, listItems: List<Item>) {
        state = state.copy(moviesScreenState = moviesScreenState, listItems = listItems)
    }

    private fun setRecyclerMapState(mapStateFlag: Boolean) {
        state = state.copy(isLinearLayoutRecyclerMap = mapStateFlag)
    }

    private fun setAdapterItems(listItems: List<Item>) {
        state = state.copy(listItems = listItems)
    }

    private fun navigateToDetails(domainMovie: DomainMovie) {
        sendEvent(
            NavigateToEvent(
                MoviesFragmentDirections.actionToDetails(domainMovie.id)
            )
        )
    }
}