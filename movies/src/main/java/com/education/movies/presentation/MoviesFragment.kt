package com.education.movies.presentation

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.observe
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.movies.R
import com.education.movies.di.MoviesComponent
import com.education.movies.domain.entity.MoviesListState
import com.education.movies.domain.entity.MoviesViewState
import com.education.search.MovieAdapter
import com.education.search.RxSearchFlowable
import com.education.search.entity.Movie
import kotlinx.android.synthetic.main.movies_fragment.*
import javax.inject.Inject


class MoviesFragment : BaseFragment(R.layout.movies_fragment) {

    companion object {
        fun newInstance() = MoviesFragment()
    }

    @Inject
    lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var adapter: MovieAdapter

    private val viewModel: MoviesViewModel by viewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        MoviesComponent.create((requireActivity().application as AppWithComponent).getComponent())
            .inject(this)
        super.onAttach(context)
    }

    override fun initViewElements(view: View) {
        val editText = searchInputLayout.editText as EditText
        viewModel.initSearchMovies(RxSearchFlowable.fromView(editText) { hideKeyboard() })

        searchInputLayout.setEndIconOnClickListener {
            searchInputLayout.editText?.setText("")
            viewModel.onClearTextIconClick()
        }

        moviesRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MovieAdapter(resources.getColor(R.color.green_color)) {
            navigateTo(MoviesFragmentDirections.actionFilmsToDetails(it.title))
        }

        moviesRecyclerView.adapter = adapter

        observe(viewModel.liveState, ::renderView)
        observe(viewModel.eventsQueue, ::onEvent)

        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                hideKeyboard()
            }
        })

        moviesConstrainContainer.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun onEvent(event: Event) {
        onFragmentEvent(event, moviesConstrainContainer)
    }

    private fun renderView(moviesViewState: MoviesViewState) {
        when (moviesViewState.moviesListState) {
            MoviesListState.EMPTY -> {
                renderNotFoundScreen()
            }
            MoviesListState.NONE_EMPTY -> {
                renderRecyclerView(moviesViewState.movies)
            }
            MoviesListState.CREATED -> {
                initialState()
            }
        }
    }

    private fun renderRecyclerView(moviesList: List<Movie>) {
        adapter.listMovies = moviesList

        if (notfoundImageView.visibility != View.GONE)
            notfoundImageView.visibility = View.GONE

        if (notfoundTextView.visibility != View.GONE)
            notfoundTextView.visibility = View.GONE

        if (moviesRecyclerView.visibility != View.VISIBLE)
            moviesRecyclerView.visibility = View.VISIBLE
    }

    private fun renderNotFoundScreen() {
        if (moviesRecyclerView.visibility != View.GONE)
            moviesRecyclerView.visibility = View.GONE

        notfoundTextView.visibility = View.VISIBLE
        notfoundImageView.visibility = View.VISIBLE
    }

    private fun initialState() {
        if (notfoundImageView.visibility != View.GONE)
            notfoundImageView.visibility = View.GONE

        if (notfoundTextView.visibility != View.GONE)
            notfoundTextView.visibility = View.GONE

        if (moviesRecyclerView.visibility != View.GONE)
            moviesRecyclerView.visibility = View.GONE
    }
}
