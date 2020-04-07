package com.education.movies.presentation

import android.content.Context
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.core_api.GRID_LAYOUT_ITEM_COUNT
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.observe
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.uievent.Event
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
import timber.log.Timber
import javax.inject.Inject

class MoviesFragment : BaseFragment(R.layout.movies_fragment) {

    companion object {
        fun newInstance() = MoviesFragment()
    }

    @Inject
    lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
        Timber.d("initViewElements")
        viewModel.initResources(
            resources.getColor(R.color.green_color),
            resources.getString(R.string.ru_locale_min),
            resources.getDrawable(R.drawable.image_placeholder))
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
            Timber.d("changeVisibilityForView for progress bar")
            changeVisibilityForView(progressBar, View.GONE)
        }

        groupAdapter = viewModel.adapter
        moviesRecyclerView.apply {
            layoutManager = if (viewModel.recyclerMapState.value == true)
                LinearLayoutManager(context)
            else
                GridLayoutManager(context, GRID_LAYOUT_ITEM_COUNT)
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
    }

    private fun onEvent(event: Event) {
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
                changeVisibilityForView(progressBar, View.VISIBLE)
            }
        }
    }

    private fun renderRecyclerView() {

        changeVisibilityForView(progressBar, View.GONE)

        changeVisibilityForView(notfoundImageView, View.GONE)

        changeVisibilityForView(notfoundTextView, View.GONE)

        changeVisibilityForView(moviesRecyclerView, View.VISIBLE)
    }

    private fun changeVisibilityForView(view: View, visibleFlag: Int) {
        when (visibleFlag) {
            View.GONE -> {
                if (!view.isGone)
                    view.visibility = View.GONE
            }
            View.VISIBLE -> {
                if (!view.isVisible)
                    view.visibility = View.VISIBLE
            }
        }
    }

    private fun renderNotFoundScreen() {
        changeVisibilityForView(progressBar, View.GONE)

        changeVisibilityForView(moviesRecyclerView, View.GONE)

        changeVisibilityForView(notfoundImageView, View.VISIBLE)

        changeVisibilityForView(notfoundTextView, View.VISIBLE)
    }

    private fun cleanState() {
        changeVisibilityForView(progressBar, View.GONE)

        changeVisibilityForView(moviesRecyclerView, View.GONE)

        changeVisibilityForView(notfoundImageView, View.GONE)

        changeVisibilityForView(notfoundTextView, View.GONE)
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
        val currentPos =
            if (moviesRecyclerView.layoutManager is GridLayoutManager)
                (moviesRecyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            else
                (moviesRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        if (isLineLayoutManager) {
            if (moviesRecyclerView.layoutManager is GridLayoutManager)
                moviesRecyclerView.layoutManager = LinearLayoutManager(context)
        } else if (moviesRecyclerView.layoutManager !is GridLayoutManager) {
            moviesRecyclerView.layoutManager = GridLayoutManager(context, GRID_LAYOUT_ITEM_COUNT)
        }

        // GridLayout расширяет LinearLayoutManager
        if (currentPos != -1)
            (moviesRecyclerView.layoutManager as LinearLayoutManager).scrollToPosition(currentPos)
    }

    private fun changeRecyclerMapIcon(isLineLayoutManager: Boolean) {
        if (!isLineLayoutManager) {
            recyclerMap.setImageResource(R.drawable.ic_to_list_map)
        }
        else {
            recyclerMap.setImageResource(R.drawable.ic_to_tile_map)
        }
    }

    private fun changeRecyclerMap() {
        if (moviesRecyclerView.layoutManager is GridLayoutManager) {
            val position = (moviesRecyclerView.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
            moviesRecyclerView.layoutManager = LinearLayoutManager(context)
            (moviesRecyclerView.layoutManager as LinearLayoutManager).scrollToPosition(position)
        } else {
            val position = (moviesRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            moviesRecyclerView.layoutManager = GridLayoutManager(context, GRID_LAYOUT_ITEM_COUNT)
            (moviesRecyclerView.layoutManager as GridLayoutManager).scrollToPosition(position)
        }
    }

    override fun onDestroyView() {
        if (moviesRecyclerView.adapter != null)
            moviesRecyclerView.swapAdapter(null, true)
        super.onDestroyView()
    }
}
