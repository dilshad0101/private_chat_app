package com.app.prvtmsg.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun FormatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(date)

}