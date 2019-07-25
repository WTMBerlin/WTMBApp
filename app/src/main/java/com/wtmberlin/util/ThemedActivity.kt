package com.wtmberlin.util

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.wtmberlin.R
import org.koin.android.ext.android.inject

@SuppressLint("Registered")
open class ThemedActivity : AppCompatActivity() {

    internal val mUiPreference: UiPreference by inject()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        if (mUiPreference.isDarkEnabled) {
            setTheme(R.style.AppThemeNightMode)
        } else {
            setTheme(R.style.AppThemeLightMode)
        }
        super.onCreate(savedInstanceState, persistentState)
    }

    internal fun switchMode() {
        mUiPreference.isDarkEnabled = !mUiPreference.isDarkEnabled
        recreate()
    }

}