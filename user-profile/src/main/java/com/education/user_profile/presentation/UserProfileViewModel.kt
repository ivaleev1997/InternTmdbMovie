package com.education.user_profile.presentation

import androidx.lifecycle.MutableLiveData
import com.education.core_api.data.network.exception.UnAuthorizedException
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.delegate
import com.education.core_api.extension.isNetworkException
import com.education.core_api.extension.schedulersIoToMain
import com.education.core_api.presentation.uievent.TryLaterEvent
import com.education.core_api.presentation.uievent.NoNetworkEvent
import com.education.core_api.presentation.uievent.UnAuthorizedEvent
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.user_profile.domain.ProfileUseCase
import com.education.user_profile.domain.entity.UserProfileViewState
import timber.log.Timber

class UserProfileViewModel(
    private val profileUseCase: ProfileUseCase,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    val liveState = MutableLiveData(createInitialState())
    private var state: UserProfileViewState by liveState.delegate()

    private fun createInitialState() = UserProfileViewState("", "")

    fun loadUserProfile() {
        profileUseCase.loadUserProfile().schedulersIoToMain(schedulersProvider).subscribe(
            { profile ->
                state = state.copy(userName = profile.name, userEmail = profile.email)
            },
            { error ->
                when {
                    error.isNetworkException() -> sendEvent(
                        NoNetworkEvent()
                    )

                    error is UnAuthorizedException -> sendEvent(
                        UnAuthorizedEvent()
                    )

                    else -> sendEvent(
                        TryLaterEvent()
                    )
                }
                Timber.d(error)
            }
        ).autoDispose()
    }
}
