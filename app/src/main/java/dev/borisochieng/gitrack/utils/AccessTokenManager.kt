package dev.borisochieng.gitrack.utils

import android.content.Context
import android.content.SharedPreferences

object AccessTokenManager {
    private const val PREF_NAME = "AccessTokenPrefs"
    private const val KEY_ACCESS_TOKEN = "accessToken"


    //create shared preferences
    private fun getSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            PREF_NAME, Context.MODE_PRIVATE
        )

    //save access token
    fun saveAccessToken(context: Context, accessToken: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    //fetch accessToken
    fun getAccessToken(context: Context): String? =
        getSharedPreferences(context).getString(KEY_ACCESS_TOKEN, null)

    // Clear access token from SharedPreferences (for logging out)
    fun clearAccessToken(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_ACCESS_TOKEN)
        editor.apply()
    }


}