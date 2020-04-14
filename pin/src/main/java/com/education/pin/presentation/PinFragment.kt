package com.education.pin.presentation

import androidx.recyclerview.widget.GridLayoutManager
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.view.recycler.AutoCleanableRecyclerView
import com.education.pin.R
import com.education.pin.domain.entity.Number
import com.education.pin.domain.entity.PinViewState
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.hidden_display.*

abstract class PinFragment(layoutId: Int) : BaseFragment(layoutId) {

    protected val groupAdapter = GroupAdapter<ViewHolder>().apply {
        spanCount = 3
    }

    protected fun initRecyclerView(recyclerView: AutoCleanableRecyclerView) {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, groupAdapter.spanCount)
            adapter = groupAdapter
        }
    }

    protected fun setAllHidePinIconsNotPressed() {
        imageView1.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
        imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
        imageView3.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
        imageView4.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
    }

    protected fun setAllHidePinIconsError() {
        imageView1.setImageDrawable(resources.getDrawable(R.drawable.ic_number_wrong))
        imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_number_wrong))
        imageView3.setImageDrawable(resources.getDrawable(R.drawable.ic_number_wrong))
        imageView4.setImageDrawable(resources.getDrawable(R.drawable.ic_number_wrong))
    }

    protected fun updateAdapter(listItems: List<Item>) {
        groupAdapter.update(listItems)

    }

    protected fun handleEnterKeyStatusEnter(number: Number?) {
        when(number) {
            Number.FIRST -> {
                imageView1.setImageDrawable(resources.getDrawable(R.drawable.ic_number_pressed))
            }
            Number.SECOND -> {
                imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_number_pressed))
            }
            Number.THIRD -> {
                imageView3.setImageDrawable(resources.getDrawable(R.drawable.ic_number_pressed))
            }
            Number.FOURTH -> {
                imageView4.setImageDrawable(resources.getDrawable(R.drawable.ic_number_pressed))
            }
        }
    }

    protected fun handleBackspaceKeyStatus(number: Number?) {
        when(number) {
            Number.FIRST -> {
                imageView1.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
            }
            Number.SECOND -> {
                imageView2.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
            }
            Number.THIRD -> {
                imageView3.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
            }
            Number.FOURTH -> {
                imageView4.setImageDrawable(resources.getDrawable(R.drawable.ic_number_not_pressed))
            }
        }
    }

    protected abstract fun renderViewState(pinViewState: PinViewState)
}