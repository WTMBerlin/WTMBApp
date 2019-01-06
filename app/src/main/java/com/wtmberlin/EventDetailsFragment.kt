package com.wtmberlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wtmberlin.data.Coordinates
import com.wtmberlin.databinding.EventDetailsScreenBinding
import com.wtmberlin.util.observeNotHandled
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addToCalendar.observeNotHandled(this) { addEventToCalendar(it.calendarEvent) }
        viewModel.openMaps.observeNotHandled(this) { openMaps(it.venueName, it.coordinates) }
    }

    private fun openMaps(venueName: String, coordinates: Coordinates) {
        val uri = Uri.parse("geo:${coordinates.latitude},${coordinates.longitude}?q=${Uri.encode(venueName)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.resolveActivity(context!!.packageManager)?.let {
            startActivity(intent)
        }
    }

    private fun addEventToCalendar(calendarEvent: CalendarEvent) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = Events.CONTENT_URI
            putExtra(Events.TITLE, calendarEvent.title)
            putExtra(Events.EVENT_LOCATION, calendarEvent.location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendarEvent.beginTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendarEvent.endTime)
        }

        intent.resolveActivity(context!!.packageManager)?.let {
            startActivity(intent)
        }
    }
}
