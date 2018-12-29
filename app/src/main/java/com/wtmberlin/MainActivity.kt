package com.wtmberlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wtmberlin.data.Repository
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

