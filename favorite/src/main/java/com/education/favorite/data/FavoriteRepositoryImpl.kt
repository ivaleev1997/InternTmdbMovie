package com.education.favorite.data

import com.education.core_api.data.LocalDataSource
import com.education.core_api.data.local.entuty.Movie
import com.education.core_api.data.network.TmdbMovieApi
import com.education.core_api.data.network.entity.DetailsMovie
import com.education.core_api.joinGenreArrayToString
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val tmdbMovieApi: TmdbMovieApi,
    private val localDataSource: LocalDataSource
) : FavoriteRepository {
    override fun loadFavorite(): Single<List<Movie>> {
        return fetchAndSaveFavoriteMovies()
            .andThen(getMoviesFromLocalStore())
            .onErrorResumeNext(getMoviesFromLocalStore())
    }

    private fun loadDetails(long: Long): Single<DetailsMovie> {
        return tmdbMovieApi.getDetailsMovie(long)
    }

    private fun getMoviesFromLocalStore(): Single<List<Movie>> {
        return localDataSource.getMovies()
    }

    private fun fetchAndSaveFavoriteMovies(): Completable {
        return tmdbMovieApi.getAccountInfo()
            .flatMap { account -> tmdbMovieApi.getFavoriteMovies(accountId = account.id) }
            .flattenAsObservable { moviesApiResponse -> moviesApiResponse.movies }
            .flatMapSingle { searchMovie -> loadDetails(searchMovie.id) }
            .toList().flatMapCompletable { movies ->
                saveMoviesToLocalStore(movies)
            }
    }

    private fun saveMoviesToLocalStore(listMovies: List<DetailsMovie>): Completable {
        return localDataSource.saveMovies(convertDetailsMovieToMovie(listMovies))
    }

    private fun convertDetailsMovieToMovie(detailsMoviesList: List<DetailsMovie>): List<Movie> {
        return detailsMoviesList.map { detailsMovie ->
            Movie(
                detailsMovie.id,
                detailsMovie.originalTitle,
                detailsMovie.posterPath,
                detailsMovie.overview,
                detailsMovie.releaseDate,
                detailsMovie.voteCount,
                detailsMovie.voteAverage,
                detailsMovie.title,
                detailsMovie.popularity,
                detailsMovie.runTime,
                detailsMovie.genres.joinGenreArrayToString()
            )
        }
    }
}