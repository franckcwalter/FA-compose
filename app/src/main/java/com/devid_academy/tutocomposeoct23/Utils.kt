package com.devid_academy.tutocomposeoct23

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.devid_academy.tutocomposeoct23.network.ApiInterface
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


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


object Category {
    val SPORT = 1
    val MANGA = 2
    val DIVERS = 3
}

fun Context.toast(userMessage : Int) =
    Toast.makeText(this, userMessage, Toast.LENGTH_LONG).show()


fun formatDate(creationDate : String) : String? {

    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(creationDate)?.let {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(it)
                }
}

