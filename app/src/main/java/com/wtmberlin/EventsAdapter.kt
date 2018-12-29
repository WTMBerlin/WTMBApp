package com.wtmberlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_detail_event.view.*
import java.util.*

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    private var data: List<EventsDAO> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<EventsDAO>) {
        this.data = data
        notifyDataSetChanged()
    }

    class EventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(event: EventsDAO) = with(itemView) {
            itemView.eventTitle.text = event.title
            itemView.eventDate.text = event.date
            itemView.eventDescription.text = event.description
            setOnClickListener {
                TODO("add callback")
            }
        }
    }
}