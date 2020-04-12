package com.education.search.domain

import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.joinGenreArrayToString
import com.education.core_api.toOriginalTitleYear
import com.education.core_api.toTmdbPosterPath
import com.education.search.domain.entity.Movie

open class MoviesRecyclerUseCase {

    protected fun detailsMovieMapToMovie(listDetailsMovie: List<DetailsMovie>): List<Movie> {
        return listDetailsMovie.map { detailsMovie ->
            Movie(
                detailsMovie.id,
                detailsMovie.posterPath.toTmdbPosterPath(),
                detailsMovie.title,
                detailsMovie.originalTitle + detailsMovie.releaseDate.toOriginalTitleYear(),
                detailsMovie.genres.joinGenreArrayToString(),
                detailsMovie.voteAverage,
                detailsMovie.voteCount,
                detailsMovie.runTime.toString()
            )
        }
    }
}