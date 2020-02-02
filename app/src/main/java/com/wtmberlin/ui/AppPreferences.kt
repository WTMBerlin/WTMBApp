package com.wtmberlin.ui

import android.content.Context
import android.content.SharedPreferences
import java.lang.IllegalStateException

/**
 * @author Antonina https://github.com/lomza/sharedpreferences-in-kotlin/blob/master/app/src/main/java/com/lomza/spinkotlin/AppPreferences.kt
 */
object AppPreferences {
    private const val PREFERENCE_NOTES = "Notes"
    private const val PREFERENCE_DARK_THEME = "Notes"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


    private const val NOTE = "note"
    private const val DARK_THEME_SETTING = "dark_theme_setting"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFERENCE_NOTES, MODE)
        preferences = context.getSharedPreferences(PREFERENCE_DARK_THEME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.commit()
    }

    var note: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences?.getString(NOTE, "")
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString(NOTE, value)
        }

    var darkTheme: Boolean
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getBoolean(DARK_THEME_SETTING, true)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(DARK_THEME_SETTING, value)
        }
}
