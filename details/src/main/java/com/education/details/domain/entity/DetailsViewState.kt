package com.education.details.domain.entity

data class DetailsViewState(
    val movieOverView: MovieOverView? = null,
    val loadStatus: LoadStatus = LoadStatus.LOAD,
    val favorite: Boolean? = null
)