package com.app.prvtmsg.util

import android.content.Context

const val PREFERENCE_NAME = "preference"
const val PREFERENCE_KEY = "user"
fun editPreference(context: Context, newPreference: String?){
    val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    with(sharedPreferences.edit()){
        putString(PREFERENCE_KEY, newPreference)
        apply()
    }
}

fun readPreference(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(PREFERENCE_KEY, "")
}