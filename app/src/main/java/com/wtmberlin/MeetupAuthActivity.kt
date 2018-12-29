package com.wtmberlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wtmberlin.util.observeNotHandled
import org.koin.android.viewmodel.ext.android.viewModel

class MeetupAuthActivity: AppCompatActivity() {
    private val viewModel: MeetupAuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.meetup_auth_screen)

        viewModel.navigateToMainScreen.observeNotHandled(this) { navigateToMainActivity() }

        processQueryParams()
    }

    private fun processQueryParams() {
        val uri = intent.data
        val code = uri!!.getQueryParameter("code")!!

        viewModel.processAuthResult(MeetupAuthResult(code = code, error = null))
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)
    }
}