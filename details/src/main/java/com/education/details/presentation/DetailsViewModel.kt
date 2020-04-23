package com.education.details.presentation

import androidx.lifecycle.MutableLiveData
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.delegate
import com.education.core_api.extension.mapDistinct
import com.education.core_api.extension.schedulersIoToMain
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.details.domain.DetailsUseCase
import com.education.details.domain.entity.DetailsViewState
import com.education.details.domain.entity.LoadStatus
import timber.log.Timber

class DetailsViewModel /*@AssistedInject constructor*/ (
    private val detailsUseCase: DetailsUseCase,
    private val schedulersProvider: SchedulersProvider
    // @Assisted private val movieId: Int,
    // @Assisted private val minWord: String,
    // @Assisted private val voteAverageColor: Int
) : BaseViewModel() {

    val liveState = MutableLiveData(createInitialState())
    private var state: DetailsViewState by liveState.delegate()
    val favoriteState = liveState.mapDistinct { it.favorite }

    private fun createInitialState(): DetailsViewState = DetailsViewState()

    fun loadDetails(id: Long, minWord: String) {
        detailsUseCase.loadDetails(id, minWord)
            .schedulersIoToMain(schedulersProvider)
            .subscribe({ movieOverView ->
                state = state.copy(
                    movieOverView = movieOverView,
                    loadStatus = LoadStatus.SUCCESS,
                    favorite = movieOverView.isFavorite
                )
            }, { error ->
                Timber.e(error)
                handleError(error)
            }).autoDispose()
    }

    fun onFavoriteClicked() {
        Timber.d("favorite - 1")

        state.movieOverView?.let { movie ->
            val favoriteFlag = !movie.isFavorite
            state = state.copy(
                movieOverView = movie.copy(isFavorite = favoriteFlag),
                favorite = favoriteFlag
            )
            detailsUseCase.changeFavorite(movie.id, favoriteFlag)
                .schedulersIoToMain(schedulersProvider)
                .subscribe({}, { error ->
                    state = state.copy(
                        movieOverView = movie.copy(isFavorite = !favoriteFlag),
                        favorite = !favoriteFlag
                    )
                    handleError(error)
                })
                .autoDispose()
        }
    }

//    @AssistedInject.Factory
//    interface Factory {
//        fun get(
//            movieId: Int,
//            minWord: String,
//            voteAverageColor: Int
//        ) : DetailsViewModel
//    }
}
