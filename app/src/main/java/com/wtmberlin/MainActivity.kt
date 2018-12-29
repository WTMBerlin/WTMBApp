package com.wtmberlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wtmberlin.data.accessToken


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (accessToken == null) {
            //authorizeUser()
        }
    }

    private fun authorizeUser() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(
            "https://secure.meetup.com/oauth2/authorize?client_id=8h22npmn9nfg58mco97blumdg9&response_type=code&redirect_uri=http%3A%2F%2Fwtmberlin.com%2Fandroid-app")
        )
        startActivity(browserIntent)
    }
}

