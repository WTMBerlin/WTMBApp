package com.wtmberlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.wtmberlin.R
import kotlinx.android.synthetic.main.main_screen.*


class MainActivity : AppCompatActivity() {
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


    }

}

