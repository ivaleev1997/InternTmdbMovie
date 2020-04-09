package com.education.movies.presentation

import android.content.Context
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.makeGone
import com.education.core_api.extension.makeVisible
import com.education.core_api.extension.observe
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.uievent.NoNetworkEvent
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.movies.R
import com.education.movies.di.MoviesComponent
import com.education.movies.domain.entity.MoviesScreenState
import com.jakewharton.rxbinding2.widget.textChanges
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.BackpressureStrategy
import kotlinx.android.synthetic.main.movies_fragment.*
import kotlinx.android.synthetic.main.movies_main_content.*
import timber.log.Timber
import javax.inject.Inject

class MoviesFragment : BaseFragment(R.layout.movies_fragment) {

    companion object {
        fun newInstance() = MoviesFragment()
    }

    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoviesViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var groupAdapter: GroupAdapter<ViewHolder>

    override fun onAttach(context: Context) {
        MoviesComponent.create((requireActivity().application as AppWithComponent).getComponent())
            .inject(this)
        super.onAttach(context)
    }

    override fun initViewElements(view: View) {
        mainContent.makeVisible()

        viewModel.initSearchMovies(
            loginTextEdit.textChanges()
                .map { it.toString() }
                .toFlowable(
                    BackpressureStrategy.LATEST
                )
        )

        searchInputLayout.setEndIconOnClickListener {
            searchInputLayout.editText?.setText("")
            viewModel.onClearTextIconClick()
            progressBar.makeGone()
        }

        groupAdapter = viewModel.adapter

        moviesRecyclerView.apply {
            layoutManager = if (viewModel.recyclerMapState.value == true)
                linearLayoutManager
            else
                gridLayoutManager
            adapter = groupAdapter
        }

        observe(viewModel.moviesScreenState, ::renderView)
        observe(viewModel.recyclerMapState, ::renderRecyclerMapState)
        observe(viewModel.adapterItemsState, ::updateAdapter)
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

        setRecyclerChangeMapListener()

        setOnErrorRepeatListener()
    }

    private fun setOnErrorRepeatListener() {
        networkErrorView.setOnRepeatClickListener {
            networkErrorView.makeGone()
            mainContent.makeVisible()
            // TODO retry request
            //val currentQuery = "${loginTextEdit.text} "
            //loginTextEdit.setText(currentQuery)
            //loginTextEdit.setText(loginTextEdit.text?.trim())
        }
    }

    private fun onEvent(event: Event) {
        Timber.d("onEvent")
        if (event is NoNetworkEvent) {
            progressBar.makeGone()
            mainContent.makeGone()
            cleanState()
            networkErrorView.makeVisible()
            hideKeyboard()
        } else
            onFragmentEvent(event, moviesConstrainContainer)
    }

    private fun renderView(moviesScreenState: MoviesScreenState) {
        when (moviesScreenState) {
            MoviesScreenState.EMPTY -> {
                Timber.d("EMPTY")
                renderNotFoundScreen()
            }
            MoviesScreenState.NONE_EMPTY -> {
                Timber.d("NONE_EMPTY")
                renderRecyclerView()
            }
            MoviesScreenState.CLEAN -> {
                Timber.d("CLEAN")
                cleanState()
            }
            MoviesScreenState.ON_SEARCH -> {
                Timber.d("ON_SEARCH")
                progressBar.makeVisible()
            }
        }
    }

    private fun renderRecyclerView() {
        progressBar.makeGone()
        notfoundImageView.makeGone()
        notfoundTextView.makeGone()
        moviesRecyclerView.makeVisible()
    }

    private fun renderNotFoundScreen() {
        progressBar.makeGone()
        moviesRecyclerView.makeGone()
        notfoundImageView.makeVisible()
        notfoundTextView.makeVisible()
    }

    private fun cleanState() {
        progressBar.makeGone()
        moviesRecyclerView.makeGone()
        notfoundImageView.makeGone()
        notfoundTextView.makeGone()
    }

    private fun setRecyclerChangeMapListener() {
        recyclerMap.setOnClickListener {
            viewModel.onReMapRecyclerClick()
        }
    }

    private fun updateAdapter(listItems: List<Item>) {
        groupAdapter.update(listItems)
    }

    private fun renderRecyclerMapState(isLineLayoutManager: Boolean) {
        changeRecyclerMapIcon(isLineLayoutManager)
        moviesRecyclerView.changeLayoutManager(isLineLayoutManager)
    }

    private fun changeRecyclerMapIcon(isLineLayoutManager: Boolean) {
        if (!isLineLayoutManager)
            recyclerMap.setImageResource(R.drawable.ic_to_list_map)
        else
            recyclerMap.setImageResource(R.drawable.ic_to_tile_map)

    }

    override fun onDestroyView() {
        if (moviesRecyclerView.adapter != null)
            moviesRecyclerView.swapAdapter(null, true)
        super.onDestroyView()
    }
}
