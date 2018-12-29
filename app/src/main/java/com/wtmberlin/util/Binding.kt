package com.wtmberlin.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wtmberlin.EventsAdapter
import com.wtmberlin.EventsAdapterItem
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

private val mediumDateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

fun LocalDateTime?.toMediumFormat() = this?.format(mediumDateFormatter)

@BindingAdapter("data")
fun setData(recyclerView: RecyclerView, items: List<EventsAdapterItem>?) {
    if (recyclerView.adapter is EventsAdapter && items != null) {
        (recyclerView.adapter as EventsAdapter).submitList(items)
    }
}