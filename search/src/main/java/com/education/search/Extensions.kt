package com.education.search

const val GREEN_MIN_VOTE_AVERAGE = 7.5

fun isShouldBeGreen(voteAverage: Double): Boolean = voteAverage >= GREEN_MIN_VOTE_AVERAGE
