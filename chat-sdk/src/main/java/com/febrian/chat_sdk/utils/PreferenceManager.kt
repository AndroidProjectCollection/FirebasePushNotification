package com.febrian.chat_sdk.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.febrian.firebasenotification.utils.Constant

class PreferenceManager(c : Context) {
    private var sharedPreference: SharedPreferences =
        c.getSharedPreferences(Constant.sharedPreferences, Context.MODE_PRIVATE)

    @SuppressLint("CommitPrefEdits")
    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreference.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreference.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        return sharedPreference.getString(key, null).toString()
    }

    fun clear() {
        val editor = sharedPreference.edit()
        editor.clear()
        editor.apply()
    }
}