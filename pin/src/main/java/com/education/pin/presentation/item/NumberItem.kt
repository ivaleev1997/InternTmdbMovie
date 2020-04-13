package com.education.pin.presentation.item

import com.education.pin.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.number_item.view.*

class NumberItem(
    private val number: Int? = null,
    private val callback: ((Int?) -> Unit)? = null
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        with(viewHolder.itemView) {
            if (number != null) {
                numberTextView.text = number.toString()
                numberTextView.setOnClickListener {
                    val number = numberTextView.text?.toString()?.toIntOrNull()
                    callback?.invoke(number)
                }
            } else {
                numberTextView.isClickable = false
            }
        }
    }

    override fun getLayout(): Int = R.layout.number_item
}