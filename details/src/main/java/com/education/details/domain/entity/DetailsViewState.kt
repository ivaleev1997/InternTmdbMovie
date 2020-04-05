package com.education.details.domain.entity

data class DetailsViewState(
    val movieOverView: List<MovieOverView>,
    val loadStatus: LoadStatus
)