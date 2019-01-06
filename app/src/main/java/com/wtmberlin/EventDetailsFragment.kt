package com.wtmberlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.wtmberlin.databinding.EventDetailsScreenBinding
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EventDetailsFragment : Fragment() {
    private lateinit var eventId: String
    private val viewModel: EventDetailsViewModel by viewModel { parametersOf(eventId) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        eventId = EventDetailsFragmentArgs.fromBundle(arguments!!).eventId

        val binding = DataBindingUtil.inflate<EventDetailsScreenBinding>(
            inflater,
            R.layout.event_details_screen,
            container,
            false
        )

        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        return binding.root
    }
}
