package com.wtmberlin.ui

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Antonina https://github.com/lomza/sharedpreferences-in-kotlin/blob/master/app/src/main/java/com/lomza/spinkotlin/AppPreferences.kt
 */
object AppPreferences {
    private const val NAME = "Notes"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


    private val NOTE = "note"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var note: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString(NOTE, "")
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString(NOTE, value)
        }
}