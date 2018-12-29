package com.wtmberlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wtmberlin.databinding.FragmentDetailStatsBinding
import com.wtmberlin.util.AdapterItem
import org.koin.android.viewmodel.ext.android.viewModel

class DetailStatsFragment : Fragment() {

    private val viewModel: DetailStatsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil
                .inflate<FragmentDetailStatsBinding>(
                    inflater,
                    R.layout.fragment_detail_stats,
                    container,
                    false
                )

        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

sealed class StatsAdapterItem : AdapterItem

data class WtmStats(
    val events2017: Int = 0,
    val events2018: Int = 0,
    val events2019: Int = 0,
    val eventsTotal: Int,
    val members: Int)