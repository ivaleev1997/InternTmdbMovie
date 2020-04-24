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
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import timber.log.Timber

class DetailsViewModel @AssistedInject constructor (
    private val detailsUseCase: DetailsUseCase,
    private val schedulersProvider: SchedulersProvider,
    @Assisted private val movieId: Long,
    @Assisted private val minWord: String
) : BaseViewModel() {

    val liveState = MutableLiveData(createInitialState())
    private var state: DetailsViewState by liveState.delegate()
    val favoriteState = liveState.mapDistinct { it.favorite }

    private fun createInitialState(): DetailsViewState = DetailsViewState()

    init {
        loadDetails()
    }

    private fun loadDetails() {
        detailsUseCase.loadDetails(movieId, minWord)
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
                .subscribe({},{ error ->
                    state = state.copy(
                        movieOverView = movie.copy(isFavorite = !favoriteFlag),
                        favorite = !favoriteFlag
                    )
                    handleError(error)
                })
                .autoDispose()
        }
    }

    fun onErrorRepeatClicked() {
        loadDetails()
    }

    @AssistedInject.Factory
    interface Factory {
        fun get(
            movieId: Long,
            minWord: String
        ) : DetailsViewModel
    }
}
