package com.wtmberlin.ui

import android.content.Intent
import android.os.Bundle
import com.wtmberlin.util.ThemedActivity

class SplashActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}
