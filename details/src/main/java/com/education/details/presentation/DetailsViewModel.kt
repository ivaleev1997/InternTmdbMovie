package com.education.details.presentation

import androidx.lifecycle.MutableLiveData
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.delegate
import com.education.core_api.extension.schedulersIoToMain
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.details.domain.DetailsUseCase
import com.education.details.domain.entity.DetailsViewState
import com.education.details.domain.entity.LoadStatus
import timber.log.Timber

class DetailsViewModel(
    private val detailsUseCase: DetailsUseCase,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    val liveState = MutableLiveData(createInitialState())
    private var state: DetailsViewState by liveState.delegate()

    private fun createInitialState(): DetailsViewState = DetailsViewState(listOf(), LoadStatus.LOAD)

    fun loadDetails(id: Long, minWord: String) {
        detailsUseCase.loadDetails(id, minWord)
            .schedulersIoToMain(schedulersProvider)
            .subscribe({ movieOverView ->
                state = state.copy(movieOverView = listOf(movieOverView), loadStatus = LoadStatus.SUCCESS)
            }, {
                Timber.e(it)
            }).autoDispose()
    }

    fun onFavoriteClicked() {
        Timber.d("favorite - 1")
        val favoriteFlag = !state.movieOverView.first().isFavorite
        state = state.copy(
            movieOverView = listOf(state.movieOverView.first().copy(isFavorite = favoriteFlag)),
            loadStatus = LoadStatus.FAVORITE
        )
        detailsUseCase.changeFavorite(state.movieOverView.first().id, favoriteFlag)
            .schedulersIoToMain(schedulersProvider)
            .subscribe({},{ error ->
                state = state.copy(
                    movieOverView = listOf(state.movieOverView.first().copy(isFavorite = !favoriteFlag)),
                    loadStatus = LoadStatus.FAVORITE
                )
                handleError(error)
            })
            .autoDispose()
    }
}
