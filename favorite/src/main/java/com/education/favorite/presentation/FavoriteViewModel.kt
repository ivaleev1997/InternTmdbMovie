package com.education.favorite.presentation

import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.mapDistinct
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.extension.schedulersIoToMain
import com.education.favorite.domain.FavoriteUseCase
import com.education.search.domain.entity.LoadFavoriteStatus
import com.education.search.domain.entity.DomainMovie
import com.education.search.presentation.MoviesRecyclerViewModel
import io.reactivex.Flowable
import timber.log.Timber
import java.util.*

class FavoriteViewModel(
    private val favoriteUseCase: FavoriteUseCase,
    private val schedulersProvider: SchedulersProvider
) : MoviesRecyclerViewModel() {

    var loadFavoritesStatus = liveState.mapDistinct { it.loadFavoritesStatus }
    var moviesRetryStatus = liveState.mapDistinct { it.moviesScreenState }
    private var favoriteListDomainMovies: List<DomainMovie> = listOf()

    private fun createInitialLoadViewState(): LoadFavoritesViewState = LoadFavoritesViewState(LoadFavoriteStatus.LOAD)

    init {
        loadRecyclerMapState()
    }

    override fun initSearchMovies(observableQuery: Flowable<String>) {
        observableQuery
            .distinctUntilChanged()
            .map { query -> query.toLowerCase(Locale.getDefault()).trim() }
            .map { query ->
                Pair(query, favoriteListDomainMovies.filterMovieByQuery(query))
            }
            .schedulersComputationToMain(schedulersProvider)
            .subscribe { (query, movies) ->
                lastFetchedQuery = query
                handleQueryAndMovies(query, movies)
            }.autoDispose()
    }

    override fun saveRecyclerMapState(isLinearLayout: Boolean) {
        favoriteUseCase.saveRecyclerMapState(isLinearLayout)
            .schedulersIoToMain(schedulersProvider)
            .subscribe()
            .autoDispose()
    }

    override fun loadRecyclerMapState() {
        favoriteUseCase.getRecyclerMapState()
            .schedulersIoToMain(schedulersProvider)
            .subscribe(::reMapRecycler) { error ->
                Timber.d(error)
            }
            .autoDispose()
    }

    fun loadFavorites() {
        favoriteUseCase.loadFavorites()
            .schedulersIoToMain(schedulersProvider)
            .subscribe({ movies ->
                favoriteListDomainMovies = movies
                setMoviesToScreenState(movies.filterMovieByQuery(lastFetchedQuery))
                state = if (movies.isEmpty()) {
                    state.copy(loadFavoritesStatus = LoadFavoriteStatus.EMPTY)
                } else {
                    state.copy(loadFavoritesStatus = LoadFavoriteStatus.NON_EMPTY)
                }
            }, { error ->
                handleError(error)
            }).autoDispose()
    }

    private fun List<DomainMovie>.filterMovieByQuery(query: String) =
        this.filter { movie ->
            movie.title.contains(query, true) || movie.originalTitle.contains(query, true)
        }

    override fun onErrorRepeatClicked() {
        super.onErrorRepeatClicked()
        loadFavorites()
    }
}
