package com.education.core_api

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

fun convertTime(timeString: String): Long {
    val timeFormat = "yyyy-MM-dd HH:mm:ss"
    return try {
        val sdf = SimpleDateFormat(timeFormat)
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val sdfOutPutToSend = SimpleDateFormat(timeFormat)
        sdfOutPutToSend.timeZone = TimeZone.getDefault()

        val date = sdf.parse(timeString)
        date?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}

    fun <T> RecyclerView.Adapter<out RecyclerView.ViewHolder>.autoNotify(old: List<T>, new: List<T>, compare: (T, T) -> Boolean) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(old[oldItemPosition], new[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == new[newItemPosition]
            }

            override fun getOldListSize() = old.size

            override fun getNewListSize() = new.size
        })

        diff.dispatchUpdatesTo(this)
    }
