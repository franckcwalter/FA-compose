package com.devid_academy.tutocomposeoct23

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

object Category {
    val SPORT = 1
    val MANGA = 2
    val DIVERS = 3
}

/*TODO : changer pour Int */
fun Context.toast(userMessage : String) =
    Toast.makeText(this, userMessage, Toast.LENGTH_LONG).show()


class MyPrefs (private val sharedPreferences: SharedPreferences) {

    private val TOKEN = "token"
    private val USER_ID = "user_id"

    var user_id : Long
        set(value) = sharedPreferences.edit().putLong(USER_ID, value).apply()
        get() = sharedPreferences.getLong(USER_ID, 0)

    var token : String?
        set(value) = sharedPreferences.edit().putString(TOKEN, value).apply()
        get() = sharedPreferences.getString(TOKEN, null)

}
