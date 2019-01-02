package com.wtmberlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wtmberlin.data.accessToken

class MainActivity : AppCompatActivity(), ManageFragments, EventsFragment.OnEventChosenListener {
    override fun displayFragment() {
        displayLogo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initEventsFragment()


        if (accessToken == null) {
            //authorizeUser()
        }
    }

    private fun initEventsFragment() {
        val eventsFragment: EventsFragment = EventsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, eventsFragment)
            .addToBackStack(null)
            .commit()
        eventsFragment.setOnEventChosenListner(this)
    }

    private fun authorizeUser() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "https://secure.meetup.com/oauth2/authorize?client_id=8h22npmn9nfg58mco97blumdg9&response_type=code&redirect_uri=http%3A%2F%2Fwtmberlin.com%2Fandroid-app"
            )
        )
        startActivity(browserIntent)
    }

    override fun displayLogo() {

        val logoFragment = LogoFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, logoFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun displayEventDetails() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayEvents() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayStats() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayInfo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

