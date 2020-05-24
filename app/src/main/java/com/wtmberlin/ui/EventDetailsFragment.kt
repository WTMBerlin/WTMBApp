package com.wtmberlin.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wtmberlin.R
import com.wtmberlin.data.Coordinates
import com.wtmberlin.databinding.EventDetailsScreenBinding
import com.wtmberlin.util.observeNotHandled
import kotlinx.android.synthetic.main.event_details_screen.*
import kotlinx.android.synthetic.main.event_details_screen.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EventDetailsFragment : Fragment() {
    private lateinit var eventId: String
    private val viewModel: EventDetailsViewModel by viewModel { parametersOf(eventId) }
    private val baseUrl = "https://www.meetup.com/de-DE/Women-Techmakers-Berlin/"
    private val eventsEndpoint = "/events/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        eventId = EventDetailsFragmentArgs.fromBundle(requireArguments()).eventId

        val binding = DataBindingUtil.inflate<EventDetailsScreenBinding>(
            inflater,
            R.layout.event_details_screen,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addToCalendar.observeNotHandled(this) { addEventToCalendar(it) }
        viewModel.openMaps.observeNotHandled(this) { openMaps(it.venueName, it.coordinates) }
        viewModel.openMeetupPage.observeNotHandled(this) { openMeetupPage(it.url) }

        event_name_text.setOnClickListener { shareEvent(eventId) }

        view.event_photo.setOnClickListener {
            findNavController().navigate(R.id.notes_screen)
        }
    }

    private fun openMaps(venueName: String, coordinates: Coordinates) {
        val uri = Uri.parse("geo:${coordinates.latitude},${coordinates.longitude}?q=${Uri.encode(venueName)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(intent)
        }
    }

    private fun addEventToCalendar(calendarEvent: AddToCalendarEvent) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = Events.CONTENT_URI
            putExtra(Events.TITLE, calendarEvent.title)
            putExtra(Events.EVENT_LOCATION, calendarEvent.location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendarEvent.beginTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendarEvent.endTime)
        }

        intent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(intent)
        }
    }

    private fun openMeetupPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        startActivity(intent)
    }

    private fun shareEvent(event: String) {
        activity?.let {
            ShareCompat.IntentBuilder
                .from(it)
                .setType("text/plain")
                .setChooserTitle("WTM Berlin Meetup")
                .setText(baseUrl + eventsEndpoint + event)
                .startChooser()
        }
    }
}
