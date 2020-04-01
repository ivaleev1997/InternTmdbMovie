package com.education.core_api.presentation.uievent

interface Event

data class ErrorMessageEvent(
    val errorMessage: String
) : Event

data class MessageEvent(
    val message: String
) : Event

interface NoNetworkEvent : Event