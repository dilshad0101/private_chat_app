package com.app.prvtmsg

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun FormatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("d MMM HH:mm", Locale.getDefault())
    return dateFormat.format(date)

}