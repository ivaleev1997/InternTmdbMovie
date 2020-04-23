package com.education.movies.domain

import com.education.core_api.data.network.entity.DetailsMovie
import com.education.movies.data.repository.MoviesRepository
import com.education.search.domain.entity.DomainMovie
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    fun searchQuery(query: String): Single<Pair<String, List<DomainMovie>>> {
        return if (query.isBlank())
            Single.just(Pair(query, listOf()))
        else {
            moviesRepository.search(query)
                .flattenAsObservable { movieResponse -> movieResponse.movies }
                .flatMapSingle { searchMovie -> moviesRepository.loadDetails(searchMovie.id) }
                .toList()
                .map { listDetailsMovie -> detailsMapToPair(query, listDetailsMovie) }
        }
    }

    fun getRecyclerMapState(): Single<Boolean> {
        return moviesRepository.getRecyclerMapState()
    }

    fun saveRecyclerMapState(isLinearLayout: Boolean): Completable {
        return moviesRepository.saveRecyclerMapState(isLinearLayout)
    }

    private fun detailsMapToPair(
        query: String,
        listDetailsMovie: List<DetailsMovie>
    ): Pair<String, List<DomainMovie>> {
        return Pair(query,
            listDetailsMovie.map { detailsMovie -> DomainMovie(detailsMovie) }
        )
    }
}
