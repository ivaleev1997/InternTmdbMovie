package com.education.core_api

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