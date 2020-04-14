package com.education.core_api.presentation.uievent

import androidx.navigation.NavDirections

interface Event

data class ErrorMessageEvent(
    val errorMessage: String
) : Event

data class MessageEvent(
    val message: String
) : Event

class NoNetworkEvent : Event

class UnAuthorizedEvent : Event

class LogoutEvent : Event

class TryLaterEvent : Event

class RootedDeviceEvent: Event

data class NavigateToEvent(
    val navDirections: NavDirections
) : Event
