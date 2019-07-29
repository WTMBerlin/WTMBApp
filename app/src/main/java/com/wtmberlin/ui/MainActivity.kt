package com.wtmberlin.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.wtmberlin.R
import kotlinx.android.synthetic.main.main_screen.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(nav_view.menu, drawer_layout)

        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }

    fun openMeetup(view: View) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(resources.getString(R.string.social_media_link_meetup))
        startActivity(openURL)
    }

    fun openTwitter(view: View) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(resources.getString(R.string.social_media_link_twitter))
        startActivity(openURL)
    }

    fun shareMeetup(view: View) {
        ShareCompat.IntentBuilder
            .from(this)
            .setType("text/plain")
            .setChooserTitle(getString(R.string.sharing_meetup_description))
            .setText(resources.getString(R.string.social_media_link_meetup))
            .startChooser()
    }

    fun closeDrawer(view: View) {
        drawer_layout.closeDrawer(GravityCompat.START)
    }
}

