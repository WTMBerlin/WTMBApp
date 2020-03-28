package com.wtmberlin.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.wtmberlin.R
import kotlinx.android.synthetic.main.main_screen.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        val bundle = Bundle()
        val isDarkModeEnabled = AppPreferences.darkTheme
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        setContentView(R.layout.main_screen)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(nav_view.menu, drawer_layout)


        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Hello world crashlytics")
        bundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, "WTM BERLIN")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
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

    fun openReviews(view: View) {
        view.findNavController().navigate(CommunityLocalFragmentDirections.startReviewsScreen())
    }

    fun closeDrawer(view: View) = drawer_layout.closeDrawer(GravityCompat.START)


    fun enableDarkMode(view: View) {
        AppPreferences.darkTheme = true
        restartApplication()
    }

    fun disableDarkMode(view: View) {
        AppPreferences.darkTheme = false
        restartApplication()
    }

    private fun restartApplication() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        exitProcess(0)
    }

    fun openBlogContributions(view: View) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data =
            Uri.parse(resources.getString(R.string.social_media_link_github_contributions))
        startActivity(openURL)
    }

    fun rotate(kidsToy: View) {
        kidsToy.animate()
            .rotation(
                kidsToy.rotation + 130.0f
            )
    }
}

