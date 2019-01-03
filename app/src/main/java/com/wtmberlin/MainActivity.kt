package com.wtmberlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.transaction
import com.google.android.material.navigation.NavigationView
import com.wtmberlin.data.accessToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ManageFragments, EventsFragment.OnEventSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initNavigationDrawer()
        initEventsFragment()

        if (accessToken == null) {
            //authorizeUser()
        }
    }

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        initActionBar()
    }

    private fun initActionBar() {
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    private fun initNavigationDrawer() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawer_layout.closeDrawers()
            when (menuItem.itemId) {
                R.id.menu_item_social_media -> displaySocialMedia()
                R.id.menu_item_wtm_berlin -> displayLocalCommunityInfo()
                R.id.menu_item_wtm -> displayGlobalCommunityInfo()
                R.id.menu_item_wtm_stats -> displayStats()
                R.id.menu_item_wtm_events -> displayEvents()
                R.id.menu_item_collaborations -> displayCollaborationPartners()
            }
            true
        }
    }

    private fun authorizeUser() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "https://secure.meetup.com/oauth2/authorize?client_id=8h22npmn9nfg58mco97blumdg9&response_type=code&redirect_uri=http%3A%2F%2Fwtmberlin.com%2Fandroid-app"
            )
        )
        startActivity(browserIntent)
    }

    private fun initEventsFragment() {
        val eventsFragment: EventsFragment = displayEventsFragment()
        eventsFragment.setOnEventChosenListener(this)
    }

    private fun displayEventsFragment(): EventsFragment {
        val eventsFragment = EventsFragment()
        supportFragmentManager.transaction(allowStateLoss = false) {
            replace(R.id.fragment_container, eventsFragment, "display all events")
        }
        return eventsFragment
    }

    override fun displayEventDetails() {
        Toast.makeText(this,"Event details will be implemented soon", Toast.LENGTH_LONG).show()
    }

    override fun displayEvents() {
        displayEventsFragment()
    }

    override fun displayStats() {
        Toast.makeText(this,"Stats will be implemented soon", Toast.LENGTH_LONG).show()
    }

    override fun displayLocalCommunityInfo() {
        val infoFragment = CommunityInfoFragment()
        supportFragmentManager.transaction(allowStateLoss = false) {
            replace(R.id.fragment_container, infoFragment, "display info about wtmb")
        }
    }

    override fun displaySocialMedia() {
        val socialMediaFragment = SocialMediaFragment()
        supportFragmentManager.transaction(allowStateLoss = false) {
            replace(R.id.fragment_container, socialMediaFragment, "display social media")
        }
    }

    private fun displayGlobalCommunityInfo() {
        //Toast.makeText(this, "Info about Women Techmakers program will be implemented soon", Toast.LENGTH_LONG).show()
        val openURL = Intent(android.content.Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://www.womentechmakers.com")
        startActivity(openURL)
    }

    private fun displayCollaborationPartners() {
        val collaborationsFragment = CollaborationsFragment()
        supportFragmentManager.transaction(allowStateLoss = false) {
            replace(R.id.fragment_container, collaborationsFragment, "display collaboration partners")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

