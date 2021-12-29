package com.example.servisesapp

import android.content.Context
import android.content.SharedPreferences

class SessionService {
    var preferences: SharedPreferences
    var editor: SharedPreferences.Editor
    var context: Context
    var PRIVATE_MODE: Int = 0

    companion object {
        val PREF_NAME = "MpipDemo"
    }

    constructor(context: Context) {
        this.context = context
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = preferences.edit()
    }


    fun saveData(key: String, value: String) {
        editor.putString(key, value)

        editor.apply()
    }

    fun getData(key: String?) : String? {
        return preferences.getString(key, "")
    }

}