package com.wtmberlin.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.wtmberlin.R
import com.wtmberlin.util.ThemedActivity
import kotlinx.android.synthetic.main.main_screen.*


class MainActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(nav_view.menu, drawer_layout)

        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))

        rotate_icon.setOnClickListener {
            it
                    .animate()
                    .rotation(
                            rotate_icon.rotation + 180.0f
                    )
        }

        this.initSwitch()
    }

    fun openMeetup(view: View) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://www.meetup.com/de-DE/women-techmakers-berlin/")
        startActivity(openURL)
    }

    private fun initSwitch() {
        val header = nav_view.getHeaderView(0)
        val lightMode = header.findViewById<ImageView>(R.id.lightImageView)
        val darkMode = header.findViewById<ImageView>(R.id.darkImageView)

        updateIcon(lightMode, darkMode)

        lightMode.setOnClickListener { switchMode() }

        darkMode.setOnClickListener { switchMode() }

    }

    private fun updateIcon(lightMode: ImageView, darkMode: ImageView) {
        if (mUiPreference.isDarkEnabled) {
            darkMode.visibility = View.VISIBLE
            lightMode.visibility = View.GONE
        } else {
            darkMode.visibility = View.GONE
            lightMode.visibility = View.VISIBLE
        }
    }
}

