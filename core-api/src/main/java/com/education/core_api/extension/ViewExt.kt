package com.education.core_api.extension

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible


fun View.makeVisible() {
    if (!this.isVisible)
        this.visibility = View.VISIBLE
}

fun View.makeGone() {
    if (!this.isGone)
        this.visibility = View.GONE
}