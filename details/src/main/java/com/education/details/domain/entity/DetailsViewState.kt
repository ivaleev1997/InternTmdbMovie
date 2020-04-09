package com.education.details.domain.entity

data class DetailsViewState(
    val movieOverView: MovieOverView?,
    val loadStatus: LoadStatus,
    val favorite: Boolean?
)