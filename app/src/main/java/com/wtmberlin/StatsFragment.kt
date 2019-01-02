package com.wtmberlin

import androidx.fragment.app.Fragment
import com.wtmberlin.util.AdapterItem

class DetailStatsFragment : Fragment()
//TODO implement VM
//private val viewModel: StatsViewModel by viewModel()
//TODO
/*
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
   val binding =
        DataBindingUtil

            .inflate<>(
                inflater,
                R.layout.fragment_stats,
                container,
                false
            )

    binding.setLifecycleOwner(this)
    binding.viewModel = viewModel

    return binding.root}*/


/*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
}}*/


sealed class StatsAdapterItem : AdapterItem

data class WtmStats(
    val events2017: Int = 0,
    val events2018: Int = 0,
    val events2019: Int = 0,
    val eventsTotal: Int,
    val members: Int
)