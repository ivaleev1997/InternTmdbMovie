package com.education.search.presentation

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.education.core_api.extension.makeGone
import com.education.core_api.extension.makeVisible
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.search.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.recycler_movies_layout.*
import timber.log.Timber

abstract class RecyclerFragment(
    layoutId: Int
) : BaseFragment(layoutId) {

    private var groupAdapter: GroupAdapter<ViewHolder> = GroupAdapter()

    protected fun setupMoviesRecycler(isLinearLayoutManager: Boolean) {
        moviesRecyclerView.apply {
            layoutManager = if (isLinearLayoutManager)
                linearLayoutManager
            else
                gridLayoutManager
            adapter = groupAdapter
        }
    }

    protected fun setupOnScrollRecyclerHideKeyboard() {
        moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                hideKeyboard()
            }
        })
    }

    protected fun makeRecyclerVisible() {
        moviesRecyclerView.makeVisible()
    }

    protected fun makeRecyclerGone() {
        moviesRecyclerView.makeGone()
    }

    protected fun updateAdapter(listItems: List<Item>) {
        Timber.d("Update adapter: ${listItems.size}")
        groupAdapter.update(listItems)
    }

    protected fun setRecyclerChangeMapListener(view: View, viewModel: MoviesRecyclerViewModel) {
        view.setOnClickListener {
            viewModel.onReMapRecyclerClick()
        }
    }

    protected fun renderRecyclerMapState(view: ImageView, isLineLayoutManager: Boolean) {
        changeRecyclerMapIcon(view, isLineLayoutManager)
        moviesRecyclerView.changeLayoutManager(isLineLayoutManager)
    }

    private fun changeRecyclerMapIcon(view: ImageView, isLineLayoutManager: Boolean) {
        if (!isLineLayoutManager)
            view.setImageResource(R.drawable.ic_to_list_map)
        else
            view.setImageResource(R.drawable.ic_to_tile_map)
    }

}