package com.wtmberlin.util

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

class UiPreference(applicationContext: Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)

    var isDarkEnabled: Boolean
        get() = sharedPref.getBoolean("DARK_MODE", false)
        set(value) {
            sharedPref.edit { putBoolean("DARK_MODE", value) }
        }

}