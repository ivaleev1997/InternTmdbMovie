package com.education.movies

import java.net.HttpURLConnection
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

const val avengersResponseBody = """{
  "page": 1,
  "total_results": 47,
  "total_pages": 3,
  "results": [
    {
      "popularity": 85.149,
      "vote_count": 17289,
      "video": false,
      "poster_path": "/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg",
      "id": 299536,
      "adult": false,
      "backdrop_path": "/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg",
      "original_language": "en",
      "original_title": "Avengers: Infinity War",
      "genre_ids": [
        28,
        12,
        878
      ],
      "title": "Avengers: Infinity War",
      "vote_average": 8.3,
      "overview": "As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.",
      "release_date": "2018-04-25"
    },
    {
      "popularity": 47.235,
      "vote_count": 21827,
      "video": false,
      "poster_path": "/cezWGskPY5x7GaglTTRN4Fugfb8.jpg",
      "id": 24428,
      "adult": false,
      "backdrop_path": "/hbn46fQaRmlpBuUrEiFqv0GDL6Y.jpg",
      "original_language": "en",
      "original_title": "The Avengers",
      "genre_ids": [
        28,
        12,
        878
      ],
      "title": "The Avengers",
      "vote_average": 7.7,
      "overview": "When an unexpected enemy emerges and threatens global safety and security, Nick Fury, director of the international peacekeeping agency known as S.H.I.E.L.D., finds himself in need of a team to pull the world back from the brink of disaster. Spanning the globe, a daring recruitment effort begins!",
      "release_date": "2012-04-25"
    },
    {
      "popularity": 56.113,
      "vote_count": 12031,
      "video": false,
      "poster_path": "/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
      "id": 299534,
      "adult": false,
      "backdrop_path": "/orjiB3oUIsyz60hoEqkiGpy5CeO.jpg",
      "original_language": "en",
      "original_title": "Avengers: Endgame",
      "genre_ids": [
        28,
        12,
        878
      ],
      "title": "Avengers: Endgame",
      "vote_average": 8.3,
      "overview": "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
      "release_date": "2019-04-24"
    }
    ]
    }"""

const val genresResponse = """{
  "genres": [
    {
      "id": 28,
      "name": "Action"
    },
    {
      "id": 12,
      "name": "Adventure"
    },
    {
      "id": 16,
      "name": "Animation"
    },
    {
      "id": 35,
      "name": "Comedy"
    },
    {
      "id": 80,
      "name": "Crime"
    },
    {
      "id": 99,
      "name": "Documentary"
    },
    {
      "id": 18,
      "name": "Drama"
    },
    {
      "id": 10751,
      "name": "Family"
    },
    {
      "id": 14,
      "name": "Fantasy"
    },
    {
      "id": 36,
      "name": "History"
    },
    {
      "id": 27,
      "name": "Horror"
    },
    {
      "id": 10402,
      "name": "Music"
    },
    {
      "id": 9648,
      "name": "Mystery"
    },
    {
      "id": 10749,
      "name": "Romance"
    },
    {
      "id": 878,
      "name": "Science Fiction"
    },
    {
      "id": 10770,
      "name": "TV Movie"
    },
    {
      "id": 53,
      "name": "Thriller"
    },
    {
      "id": 10752,
      "name": "War"
    },
    {
      "id": 37,
      "name": "Western"
    }
  ]
}"""

const val noResultsResponse = """{
  "page": 1,
  "total_results": 0,
  "total_pages": 0,
  "results": []
}"""
object AvengersDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/search/movie?query=avengers" -> MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                avengersResponseBody
            )
            "/genre/movie/list" -> MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                genresResponse
            )
            else -> MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        }
    }
}

object NoResultsDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/search/movie?query=qwertyu" -> MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                noResultsResponse
            )
            "/genre/movie/list" -> MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                genresResponse
            )
            else -> MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        }
    }
}
