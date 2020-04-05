package com.education.details.domain

import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.joinGenreArrayToString
import com.education.core_api.toTmdbPosterPath
import com.education.details.data.DetailsRepository
import com.education.details.domain.entity.MovieOverView
import io.reactivex.Single
import javax.inject.Inject

class DetailsUseCase @Inject constructor(
    private val detailsRepository: DetailsRepository
) {
    fun loadDetails(movieId: Long): Single<MovieOverView> {
        return detailsRepository.loadDetails(movieId).map(::detailsMapToMovieOverView)
    }

    private fun detailsMapToMovieOverView(detailsMovie: DetailsMovie): MovieOverView {
        return MovieOverView(
            detailsMovie.title,
            detailsMovie.originalTitle,
            detailsMovie.genres.joinGenreArrayToString(),
            detailsMovie.posterPath.toTmdbPosterPath(),
            detailsMovie.voteAverage.toString(),
            detailsMovie.voteCount.toString(),
            detailsMovie.runTime.toString(),
            detailsMovie.overview
        )
    }
}
