package com.education.favorite

import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.mapDistinct
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.extension.schedulersIoToMain
import com.education.favorite.domain.FavoriteUseCase
import com.education.search.domain.entity.LoadFavoriteStatus
import com.education.search.domain.entity.Movie
import com.education.search.presentation.MoviesRecyclerViewModel
import io.reactivex.Flowable
import java.util.*

class FavoriteViewModel(
    private val favoriteUseCase: FavoriteUseCase,
    private val schedulersProvider: SchedulersProvider
) : MoviesRecyclerViewModel() {

    var loadFavoritesStatus = liveState.mapDistinct { it.loadFavoritesStatus }
    var moviesRetryStatus = liveState.mapDistinct { it.moviesScreenState }
    private var favoriteListMovies: List<Movie> = listOf()

    override fun initSearchMovies(observableQuery: Flowable<String>) {
        observableQuery
            .distinctUntilChanged()
            .map { query -> query.toLowerCase(Locale.getDefault()).trim() }
            .map { query ->
                Pair(query, favoriteListMovies.filterMovieByQuery(query))
            }
            .schedulersComputationToMain(schedulersProvider)
            .subscribe { queryAndMovies ->
                lastFetchedQuery = queryAndMovies.first
                handleQueryAndMovies(queryAndMovies)
            }.autoDispose()
    }

    fun loadFavorites() {
        favoriteUseCase.loadFavorites()
            .schedulersIoToMain(schedulersProvider)
            .subscribe({ movies ->
                favoriteListMovies = movies
                setMoviesToScreenState(movies.filterMovieByQuery(lastFetchedQuery))
                state = if (movies.isEmpty()) {
                    state.copy(loadFavoritesStatus = LoadFavoriteStatus.EMPTY)
                } else {
                    state.copy(loadFavoritesStatus = LoadFavoriteStatus.NON_EMPTY)
                }
            },{ error ->
                handleError(error)
            }).autoDispose()
    }

    private fun List<Movie>.filterMovieByQuery(query: String) =
        this.filter { movie ->
            movie.title.contains(query, true) || movie.originalTitle.contains(query, true)
        }

    override fun onErrorRepeatClicked() {
        super.onErrorRepeatClicked()
        loadFavorites()
    }
}
