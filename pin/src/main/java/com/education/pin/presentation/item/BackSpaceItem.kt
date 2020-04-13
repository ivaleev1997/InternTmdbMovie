package com.education.pin.presentation.item

import com.education.pin.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.backspace_item.view.*

class BackSpaceItem(
    private val callback: () -> Unit
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.backspaceImage.setOnClickListener {
            callback.invoke()
        }
    }

    override fun getLayout(): Int = R.layout.backspace_item
}