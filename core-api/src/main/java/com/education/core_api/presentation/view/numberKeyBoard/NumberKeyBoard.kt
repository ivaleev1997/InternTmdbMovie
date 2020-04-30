package com.education.core_api.presentation.view.numberKeyBoard

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.education.core_api.R
import com.education.core_api.presentation.view.numberKeyBoard.item.BackSpaceItem
import com.education.core_api.presentation.view.numberKeyBoard.item.ExitItem
import com.education.core_api.presentation.view.numberKeyBoard.item.NumberItem
import com.education.core_api.presentation.view.recycler.AutoCleanableRecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.number_keyboard.view.*

class NumberKeyBoard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val KEYBOARD_NUMBERS_9 = 9
    }

    private var isExitEnabled: Boolean = false
    private var numberClickListener: ((Int) -> Unit)? = null
    private var backspaceClickListener: (() -> Unit)? = null
    private var exitClickListener: (() -> Unit)? = null

    private val groupAdapter = GroupAdapter<ViewHolder>().apply {
        spanCount = 3
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.number_keyboard, this, true)
        lateinit var attrsArray: TypedArray

        try {
            attrsArray = context.obtainStyledAttributes(attrs, R.styleable.NumberKeyBoard)
            with(attrsArray) {
                isExitEnabled = getBoolean(R.styleable.NumberKeyBoard_exitButton, false)
            }
        } finally {
            attrsArray.recycle()
        }

        initRecyclerView(numberKeyboardRecyclerView)

        groupAdapter.update(genKeyboardItems())
    }

    fun setOnNumberClickedListener(callback: (Int) -> Unit) {
        numberClickListener = callback
    }

    fun setOnExitClickListener(callback: () -> Unit) {
        exitClickListener = callback
    }

    fun setOnBackSpaceClickListener(callback: () -> Unit) {
        backspaceClickListener = callback
    }

    private fun initRecyclerView(recyclerView: AutoCleanableRecyclerView) {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, groupAdapter.spanCount)
            adapter = groupAdapter
        }
    }

    private fun genKeyboardItems(): List<Item> {
        val listNumbers = MutableList(KEYBOARD_NUMBERS_9) {
            NumberItem(
                it + 1,
                ::onNumberClicked
            ) as Item
        }.apply {
            if (isExitEnabled)
                add(
                    ExitItem(
                        ::onExitClicked
                    )
                )
            else
                add(NumberItem())
            add(
                NumberItem(
                    0,
                    ::onNumberClicked
                )
            )
            add(
                BackSpaceItem(
                    ::onBackSpaceClicked
                )
            )
        }

        return listNumbers
    }

    private fun onNumberClicked(number: Int?) {
        if (number != null)
            numberClickListener?.invoke(number)
    }

    private fun onExitClicked() {
        exitClickListener?.invoke()
    }

    private fun onBackSpaceClicked() {
        backspaceClickListener?.invoke()
    }
}
