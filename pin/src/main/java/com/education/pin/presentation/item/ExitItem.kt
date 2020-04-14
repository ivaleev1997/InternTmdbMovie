package com.education.pin.presentation.item

import com.education.pin.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.exit_item.view.*

class ExitItem(
    private val callback: (() -> Unit)
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.exitTextView.setOnClickListener {
            callback.invoke()
        }
    }

    override fun getLayout(): Int = R.layout.exit_item
}