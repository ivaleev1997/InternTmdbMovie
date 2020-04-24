package com.education.search.presentation

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.view.recycler.FromLinearToGridChangeManagerRecycler
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

    inline val Fragment.moviesRecycler: FromLinearToGridChangeManagerRecycler get() = moviesRecyclerView
    inline val Fragment.notfoundImage: ImageView get() = notfoundImageView
    inline val Fragment.notfoundText: TextView get() = notfoundTextView

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

    protected fun setFocusableEditText(editText: EditText?) {
        // Установка курсора на searchInputLayout.editText
        editText?.isFocusableInTouchMode = true
        editText?.requestFocus()
    }

    protected fun showKeyboard(editText: EditText?) {
        // Показать клавиатуру keyboard
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    protected abstract fun setupSearchInput()

    protected abstract fun setOnErrorRepeatListener()
}