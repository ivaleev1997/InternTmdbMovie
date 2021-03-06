package com.education.movies

import com.education.core_api.data.LocalDataSource
import com.education.movies.data.repository.MoviesRepository
import com.education.movies.data.repository.MoviesRepositoryImpl
import com.education.movies.domain.MoviesUseCase
import com.education.movies.presentation.MoviesViewModel
import com.education.search.domain.entity.MoviesScreenState
import com.education.search.domain.entity.MoviesViewState
import com.education.testmodule.MockTmdbMovieWebServer
import com.education.testmodule.TestSchedulersProvider
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Flowable
import io.reactivex.schedulers.TestScheduler
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MoviesJvmIntegrationTest : Spek({
    beforeGroup { enableTestMode() }
    afterGroup { disableTestMode() }

    Feature("User insert query in search edit text") {
        // region Fields
        val testScheduler = TestScheduler()
        val schedulersProvider = TestSchedulersProvider(testScheduler)
        val mockLocalDataSource = mock<LocalDataSource>() {
            on { getRecyclerMapState() } doReturn false
        }

        var repository: MoviesRepository
        var useCase: MoviesUseCase
        var mockTmdbMovieWebServer: MockTmdbMovieWebServer
        var moviesViewModel: MoviesViewModel
        // endregion Fields
        Scenario("User insert blank query and press done key on keyboard") {
            // region Fields
            mockTmdbMovieWebServer = MockTmdbMovieWebServer()
            repository = MoviesRepositoryImpl(mockTmdbMovieWebServer.tmdbMovieApi, mockLocalDataSource)
            useCase = MoviesUseCase(repository)
            moviesViewModel = MoviesViewModel(useCase, schedulersProvider)
            // endregion Fields
            var query = ""
            val isListRecyclerMap = true
            val expectedMovieViewState =
                MoviesViewState(
                    MoviesScreenState.ZERO,
                    listOf(),
                    isListRecyclerMap
                )
            Given("Set query as blank string ") {
                query = " "
            }

            When("Give viewModel queryFlowable") {
                val queryFlowable = Flowable.just("", query)
                moviesViewModel.initSearchMovies(queryFlowable)
            }

            Then("Empty list and ZERO MoviesScreenState") {
                assertSoftly {
                    assertThat(moviesViewModel.liveState.value?.moviesScreenState).isEqualTo(expectedMovieViewState.moviesScreenState)
                    assertThat(moviesViewModel.liveState.value?.listItems?.size).isEqualTo(expectedMovieViewState.listItems.size)
                }
            }
        }

        Scenario("User input right query: 'Avengers'") {
            // region Fields
            mockTmdbMovieWebServer = MockTmdbMovieWebServer()
            repository = MoviesRepositoryImpl(mockTmdbMovieWebServer.tmdbMovieApi, mockLocalDataSource)
            useCase = MoviesUseCase(repository)
            moviesViewModel = MoviesViewModel(useCase, schedulersProvider)
            // endregion Fields
            var query = "Avengers"
            val expectedMovieViewState = MoviesScreenState.NONE_EMPTY
            val expectedMoviesListSize = 2
            Given("Set query as blank string and avengers dispatcher") {
                query = "Avengers"
                mockTmdbMovieWebServer.setDispatcher(AvengersDispatcher)
            }

            When("Give viewModel queryFlowable") {
                val queryFlowable = Flowable.just("", query)
                moviesViewModel.initSearchMovies(queryFlowable)
                testScheduler.triggerActions()
            }

            Then("Should be not empty list with 2 elements and NONE_EMPTY MoviesListState") {
                assertSoftly {
                    assertThat(moviesViewModel.liveState.value?.moviesScreenState).isEqualTo(expectedMovieViewState)
                    assertThat(moviesViewModel.liveState.value?.listItems?.size).isEqualTo(expectedMoviesListSize)
                }
            }
        }

        Scenario("User input query: 'qwertyu' without results") {
            // region Fields
            mockTmdbMovieWebServer = MockTmdbMovieWebServer()
            repository = MoviesRepositoryImpl(mockTmdbMovieWebServer.tmdbMovieApi, mockLocalDataSource)
            useCase = MoviesUseCase(repository)
            moviesViewModel = MoviesViewModel(useCase, schedulersProvider)
            // endregion Fields
            var query = ""

            val expectedMovieViewState = MoviesScreenState.EMPTY
            val expectedMoviesListSize = 0
            Given("Set query as blank string and avengers dispatcher") {
                query = "qwertyu"
                mockTmdbMovieWebServer.setDispatcher(NoResultsDispatcher)
            }

            When("Give viewModel queryFlowable") {
                val queryFlowable = Flowable.just("", query)
                moviesViewModel.initSearchMovies(queryFlowable)
                testScheduler.triggerActions()
            }

            Then("Should be empty list and EMPTY MoviesListState") {
                assertSoftly {
                    assertThat(moviesViewModel.liveState.value?.moviesScreenState).isEqualTo(expectedMovieViewState)
                    assertThat(moviesViewModel.liveState.value?.listItems?.size).isEqualTo(expectedMoviesListSize)
                }
            }
        }
    }
})
