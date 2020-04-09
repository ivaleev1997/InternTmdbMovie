package com.education.details.domain

import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.data.network.entity.MovieApiResponse
import com.education.core_api.data.network.entity.SearchMovie
import com.education.core_api.data.network.entity.StatusResponse
import com.education.core_api.joinGenreArrayToString
import com.education.core_api.toOriginalTitleYear
import com.education.core_api.toTmdbPosterPath
import com.education.details.data.DetailsRepository
import com.education.details.domain.entity.MovieOverView
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class DetailsUseCase @Inject constructor(
    private val detailsRepository: DetailsRepository
) {
    private var minWord: String = ""

    fun loadDetails(movieId: Long, min: String): Single<MovieOverView> {
        minWord = min
        return detailsRepository.loadDetails(movieId).zipWith(
            detailsRepository.loadFavorites(),
            BiFunction { detailsMovie: DetailsMovie, movieApiResponse: MovieApiResponse<SearchMovie> ->
                detailsMapToMovieOverView(detailsMovie, movieApiResponse)
            })
    }

    fun changeFavorite(movieId: Long, flag: Boolean): Single<StatusResponse> {
        return detailsRepository.changeFavorite(movieId, flag)
    }

    private fun detailsMapToMovieOverView(detailsMovie: DetailsMovie, movieApiResponse: MovieApiResponse<SearchMovie>): MovieOverView {
        return MovieOverView(
            detailsMovie.id,
            detailsMovie.title,
            detailsMovie.originalTitle + detailsMovie.releaseDate.toOriginalTitleYear(),
            detailsMovie.genres.joinGenreArrayToString(),
            detailsMovie.posterPath.toTmdbPosterPath(),
            detailsMovie.voteAverage.toString(),
            detailsMovie.voteCount.toString(),
            "${detailsMovie.runTime} $minWord",
            detailsMovie.overview,
            movieApiResponse.movies.map { it.id }.contains(detailsMovie.id)
        )
    }
}
