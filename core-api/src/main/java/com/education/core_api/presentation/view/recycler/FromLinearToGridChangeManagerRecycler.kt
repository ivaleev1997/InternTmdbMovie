package com.education.core_api.presentation.view.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * RecyclerView расширяющий AutoCleanableRecyclerView и призванный инкапсулировать
 * логику изменения текущего layoutManager'а
 */
class FromLinearToGridChangeManagerRecycler @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AutoCleanableRecyclerView(context, attrs, defStyleAttr) {

    companion object {
        private const val GRID_LAYOUT_ITEM_COUNT_2 = 2
    }

    val gridLayoutManager = GridLayoutManager(context,
        GRID_LAYOUT_ITEM_COUNT_2
    )
    val linearLayoutManager = LinearLayoutManager(context)

    fun changeLayoutManager(isLineLayoutManager: Boolean) {
        val currentAdapterPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        // Здесь происходит изменение текущего мэнэджера,
        // но только при условии что это необходимо (т.е. чтобы не менять linearLayoutManager на linearLayoutManager)
        if (isLineLayoutManager) {
            if (layoutManager is GridLayoutManager)
                layoutManager = linearLayoutManager
        } else if (layoutManager !is GridLayoutManager) {
            layoutManager = gridLayoutManager
        }

        // Возвращаем отображение элементов на ту же позицию - как перед изменением отображения
        if (currentAdapterPosition != -1)
            (layoutManager as LinearLayoutManager).scrollToPosition(currentAdapterPosition)
    }
}