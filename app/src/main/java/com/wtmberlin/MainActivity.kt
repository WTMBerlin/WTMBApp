package com.wtmberlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ManageFragments {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

