package com.education.favorite.presentation

import androidx.lifecycle.MutableLiveData
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.delegate
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.extension.schedulersIoToMain
import com.education.favorite.domain.FavoriteUseCase
import com.education.favorite.domain.entity.LoadFavoriteStatus
import com.education.favorite.domain.entity.LoadFavoritesViewState
import com.education.search.domain.entity.DomainMovie
import com.education.search.presentation.MoviesRecyclerViewModel
import io.reactivex.Flowable
import java.util.*

class FavoriteViewModel(
    private val favoriteUseCase: FavoriteUseCase,
    private val schedulersProvider: SchedulersProvider
) : MoviesRecyclerViewModel() {

    val loadLiveState = MutableLiveData(createInitialLoadViewState())
    private var loadState: LoadFavoritesViewState by loadLiveState.delegate()
    private var favoriteListDomainMovies: List<DomainMovie> = listOf()

    private fun createInitialLoadViewState(): LoadFavoritesViewState = LoadFavoritesViewState(LoadFavoriteStatus.LOAD)

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

    fun loadFavorites() {
        favoriteUseCase.loadFavorites()
            .schedulersIoToMain(schedulersProvider)
            .subscribe({ movies ->
                favoriteListDomainMovies = movies
                setMoviesToScreenState(movies.filterMovieByQuery(lastFetchedQuery))
                loadState = if (movies.isEmpty()) {
                    loadState.copy(loadFavoriteStatus = LoadFavoriteStatus.EMPTY)
                } else {
                    loadState.copy(loadFavoriteStatus = LoadFavoriteStatus.NON_EMPTY)
                }
            }, { error ->
                handleError(error)
            }).autoDispose()
    }

    private fun List<DomainMovie>.filterMovieByQuery(query: String) =
        this.filter { movie ->
            movie.title.contains(query, true) || movie.originalTitle.contains(query, true)
        }
}
