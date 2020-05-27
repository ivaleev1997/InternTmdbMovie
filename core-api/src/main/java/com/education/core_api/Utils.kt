package com.education.core_api

import android.content.Context
import android.content.res.Resources.Theme
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.education.core_api.data.network.entity.Genre
import java.text.SimpleDateFormat
import java.util.*

fun List<Genre>.joinGenreArrayToString(): String {
    return this.joinToString { genre ->
        genre.genre
    }
}

fun String?.toTmdbPosterPath(): String = TMDB_IMAGE_URL + (this ?: "")

fun String.toOriginalTitleYear(): String {
    return try {
        val timeFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(timeFormat)
        val date = sdf.parse(this)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.time
        " (${calendar.get(Calendar.YEAR)})"
    } catch (e: Exception) {
        ""
    }
}

@ColorInt
fun getColorFromTheme(context: Context, resId: Int): Int {
    val typedValue = TypedValue()
    val theme: Theme = context.theme
    theme.resolveAttribute(resId, typedValue, true)

    return typedValue.data
}
