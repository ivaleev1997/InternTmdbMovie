package com.education.movies

import java.net.HttpURLConnection
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

const val avengersResponseBody = """{
  "page": 1,
  "total_results": 2,
  "total_pages": 3,
  "results": [
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
const val movie299534 = """{
  "adult": false,
  "backdrop_path": "/orjiB3oUIsyz60hoEqkiGpy5CeO.jpg",
  "belongs_to_collection": {
    "id": 86311,
    "name": "The Avengers Collection",
    "poster_path": "/yFSIUVTCvgYrpalUktulvk3Gi5Y.jpg",
    "backdrop_path": "/zuW6fOiusv4X9nnW3paHGfXcSll.jpg"
  },
  "budget": 356000000,
  "genres": [
    {
      "id": 12,
      "name": "Adventure"
    },
    {
      "id": 878,
      "name": "Science Fiction"
    },
    {
      "id": 28,
      "name": "Action"
    }
  ],
  "homepage": "https://www.marvel.com/movies/avengers-endgame",
  "id": 299534,
  "imdb_id": "tt4154796",
  "original_language": "en",
  "original_title": "Avengers: Endgame",
  "overview": "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
  "popularity": 59.678,
  "poster_path": "/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
  "production_companies": [
    {
      "id": 420,
      "logo_path": "/hUzeosd33nzE5MCNsZxCGEKTXaQ.png",
      "name": "Marvel Studios",
      "origin_country": "US"
    }
  ],
  "production_countries": [
    {
      "iso_3166_1": "US",
      "name": "United States of America"
    }
  ],
  "release_date": "2019-04-24",
  "revenue": 2797800564,
  "runtime": 181,
  "spoken_languages": [
    {
      "iso_639_1": "en",
      "name": "English"
    },
    {
      "iso_639_1": "ja",
      "name": "日本語"
    },
    {
      "iso_639_1": "xh",
      "name": ""
    }
  ],
  "status": "Released",
  "tagline": "Part of the journey is the end.",
  "title": "Avengers: Endgame",
  "video": false,
  "vote_average": 8.3,
  "vote_count": 12100
}"""

const val movie24428 = """{
  "adult": false,
  "backdrop_path": "/hbn46fQaRmlpBuUrEiFqv0GDL6Y.jpg",
  "belongs_to_collection": {
    "id": 86311,
    "name": "The Avengers Collection",
    "poster_path": "/yFSIUVTCvgYrpalUktulvk3Gi5Y.jpg",
    "backdrop_path": "/zuW6fOiusv4X9nnW3paHGfXcSll.jpg"
  },
  "budget": 220000000,
  "genres": [
    {
      "id": 878,
      "name": "Science Fiction"
    },
    {
      "id": 28,
      "name": "Action"
    },
    {
      "id": 12,
      "name": "Adventure"
    }
  ],
  "homepage": "http://marvel.com/avengers_movie/",
  "id": 24428,
  "imdb_id": "tt0848228",
  "original_language": "en",
  "original_title": "The Avengers",
  "overview": "When an unexpected enemy emerges and threatens global safety and security, Nick Fury, director of the international peacekeeping agency known as S.H.I.E.L.D., finds himself in need of a team to pull the world back from the brink of disaster. Spanning the globe, a daring recruitment effort begins!",
  "popularity": 53.728,
  "poster_path": "/cezWGskPY5x7GaglTTRN4Fugfb8.jpg",
  "production_companies": [
    {
      "id": 420,
      "logo_path": "/hUzeosd33nzE5MCNsZxCGEKTXaQ.png",
      "name": "Marvel Studios",
      "origin_country": "US"
    },
    {
      "id": 4,
      "logo_path": "/fycMZt242LVjagMByZOLUGbCvv3.png",
      "name": "Paramount",
      "origin_country": "US"
    }
  ],
  "production_countries": [
    {
      "iso_3166_1": "US",
      "name": "United States of America"
    }
  ],
  "release_date": "2012-04-25",
  "revenue": 1519557910,
  "runtime": 143,
  "spoken_languages": [
    {
      "iso_639_1": "en",
      "name": "English"
    },
    {
      "iso_639_1": "hi",
      "name": "हिन्दी"
    },
    {
      "iso_639_1": "ru",
      "name": "Pусский"
    }
  ],
  "status": "Released",
  "tagline": "Some assembly required.",
  "title": "The Avengers",
  "video": false,
  "vote_average": 7.7,
  "vote_count": 21855
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
            "/movie/299534" -> MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                movie299534
            )
            "/movie/24428" -> MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                movie24428
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
