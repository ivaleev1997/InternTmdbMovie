package com.education.movies

import com.education.movies.data.repository.MoviesRepository
import com.education.movies.data.repository.MoviesRepositoryImpl
import com.education.movies.domain.MoviesUseCase
import com.education.movies.domain.entity.MoviesListState
import com.education.movies.domain.entity.MoviesViewState
import com.education.movies.presentation.MoviesViewModel
import com.education.testmodule.MockTmdbMovieWebServer
import com.education.testmodule.TestSchedulersProvider
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MoviesJvmIntegrationTest : Spek({
    beforeGroup { enableTestMode() }
    afterGroup { disableTestMode() }

    Feature("User insert query in search edit text") {
        // region Fields
        val testScheduler = Schedulers.trampoline()
        val schedulersProvider = TestSchedulersProvider(testScheduler)

        var repository: MoviesRepository
        var useCase: MoviesUseCase
        var mockTmdbMovieWebServer: MockTmdbMovieWebServer
        var moviesViewModel: MoviesViewModel
        // endregion Fields
        Scenario("User insert blank query and press done key on keyboard") {
            // region Fields
            mockTmdbMovieWebServer = MockTmdbMovieWebServer()
            repository = MoviesRepositoryImpl(mockTmdbMovieWebServer.tmdbMovieApi)
            useCase = MoviesUseCase(repository)
            moviesViewModel = MoviesViewModel(useCase, schedulersProvider)
            // endregion Fields
            var query = ""
            val expectedMovieViewState = MoviesViewState(listOf(), MoviesListState.CREATED)
            Given("Set query as blank string ") {
                query = " "
            }

            When("Give viewModel queryFlowable") {
                val queryFlowable = Flowable.just(query)
                moviesViewModel.initSearchMovies(queryFlowable)
            }

            Then("Empty list and CREATED MoviesListState") {
                assertSoftly {
                    assertThat(moviesViewModel.liveState.value?.moviesListState).isEqualTo(expectedMovieViewState.moviesListState)
                    assertThat(moviesViewModel.liveState.value?.movies?.size).isEqualTo(expectedMovieViewState.movies.size)
                }
            }
        }

        Scenario("User input right query: 'Avengers'") {
            // region Fields
            mockTmdbMovieWebServer = MockTmdbMovieWebServer()
            repository = MoviesRepositoryImpl(mockTmdbMovieWebServer.tmdbMovieApi)
            useCase = MoviesUseCase(repository)
            moviesViewModel = MoviesViewModel(useCase, schedulersProvider)
            // endregion Fields
            var query = "Avengers"

            val expectedMovieViewState = MoviesListState.NONE_EMPTY
            val expectedMoviesListSize = 3
            Given("Set query as blank string and avengers dispatcher") {
                query = "Avengers"
                mockTmdbMovieWebServer.setDispatcher(AvengersDispatcher)
            }

            When("Give viewModel queryFlowable") {
                val queryFlowable = Flowable.just(query)
                moviesViewModel.initSearchMovies(queryFlowable)
            }

            Then("Should be not empty list with 3 elements and NONE_EMPTY MoviesListState") {
                assertSoftly {
                    assertThat(moviesViewModel.liveState.value?.moviesListState).isEqualTo(expectedMovieViewState)
                    assertThat(moviesViewModel.liveState.value?.movies?.size).isEqualTo(expectedMoviesListSize)
                }
            }
        }

        Scenario("User input query: 'qwertyu' without results") {
            // region Fields
            mockTmdbMovieWebServer = MockTmdbMovieWebServer()
            repository = MoviesRepositoryImpl(mockTmdbMovieWebServer.tmdbMovieApi)
            useCase = MoviesUseCase(repository)
            moviesViewModel = MoviesViewModel(useCase, schedulersProvider)
            // endregion Fields
            var query = ""

            val expectedMovieViewState = MoviesListState.EMPTY
            val expectedMoviesListSize = 0
            Given("Set query as blank string and avengers dispatcher") {
                query = "qwertyu"
                mockTmdbMovieWebServer.setDispatcher(NoResultsDispatcher)
            }

            When("Give viewModel queryFlowable") {
                val queryFlowable = Flowable.just(query)
                moviesViewModel.initSearchMovies(queryFlowable)
            }

            Then("Should be empty list and EMPTY MoviesListState") {
                assertSoftly {
                    assertThat(moviesViewModel.liveState.value?.moviesListState).isEqualTo(expectedMovieViewState)
                    assertThat(moviesViewModel.liveState.value?.movies?.size).isEqualTo(expectedMoviesListSize)
                }
            }
        }
    }
})
