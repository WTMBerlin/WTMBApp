package com.wtmberlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import com.wtmberlin.databinding.EventsScreenBinding
import com.wtmberlin.util.AdapterItem
import com.wtmberlin.util.BindingViewHolder
import com.wtmberlin.util.DIFF_CALLBACK
import kotlinx.android.synthetic.main.events_screen.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime

class EventsFragment : Fragment(), EventsAdapter.Callbacks {
    private val viewModel: EventsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil
                .inflate<EventsScreenBinding>(
                    inflater,
                    R.layout.events_screen,
                    container,
                    false
                )

        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events_recycler.adapter = EventsAdapter(this)
    }

    override fun onEventItemClicked(item: EventItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


class EventsAdapter(private val callbacks: Callbacks) : ListAdapter<AdapterItem, BindingViewHolder>(DIFF_CALLBACK) {
    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding =
            DataBindingUtil
                .inflate<ViewDataBinding>(
                    inflater,
                    viewType,
                    parent,
                    false
                )

        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        holder.bind(getItem(position), callbacks)
    }

    interface Callbacks {
        fun onEventItemClicked(item: EventItem)
    }
}

sealed class EventsAdapterItem : AdapterItem

object UpcomingHeaderItem : EventsAdapterItem() {
    override val id = 1L
    override val viewType = R.layout.events_upcoming_header
}

object PastHeaderItem : EventsAdapterItem() {
    override val id = 1L
    override val viewType = R.layout.events_past_header
}

data class EventItem(
    override val id: String,
    val name: String,
    val localDateTime: LocalDateTime,
    val venueName: String?
) : EventsAdapterItem() {
    override val viewType = R.layout.events_event_item
}
