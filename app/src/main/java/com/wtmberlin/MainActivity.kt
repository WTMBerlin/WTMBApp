package com.wtmberlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ManageFragments {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainViewModel = ViewModelProviders.of(this).get(ViewModelMain::class.java)
        val detailEventViewModel = ViewModelProviders.of(this).get(ViewModelDetailEvent::class.java)
        val detailStats = ViewModelProviders.of(this).get(ViewModelDetailStats::class.java)

        mainViewModel.events.observe(this, Observer {
            it?.let {
                //TODO: update events
            }
        })

        detailEventViewModel.event.observe(this, Observer {
            it?.let {
                //TODO: update event details
            }
        })

        detailStats.stats.observe(this, Observer {
            it?.let {
                //TODO: update stats details
            }
        })
    }

    override fun displayEvents() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentsContainer, main_fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun displayStats() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentsContainer, stats_fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun displayEventDetails() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentsContainer, events_fragment)
            .addToBackStack(null)
            .commit()
    }
}

