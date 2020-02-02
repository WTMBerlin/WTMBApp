package com.wtmberlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wtmberlin.R
import com.wtmberlin.databinding.StatsScreenBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SpeakersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.topic_proposals_screen,
            container, false
        )
    }
}
