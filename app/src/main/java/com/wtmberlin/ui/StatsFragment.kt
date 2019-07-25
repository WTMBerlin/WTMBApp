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

class StatsFragment : Fragment(){
    private val viewModel: StatsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding =
            DataBindingUtil
                .inflate<StatsScreenBinding>(
                    inflater,
                    R.layout.stats_screen,
                    container,
                    false
                )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}

