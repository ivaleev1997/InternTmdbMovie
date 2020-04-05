package com.education.details.data

import com.education.core_api.data.network.entity.DetailsMovie
import io.reactivex.Single

interface DetailsRepository {
    fun loadDetails(movieId: Long): Single<DetailsMovie>
}