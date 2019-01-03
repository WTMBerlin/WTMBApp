package com.wtmberlin.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wtmberlin.CollaborationsAdapter
import com.wtmberlin.EventsAdapter
import com.wtmberlin.EventsAdapterItem
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

private val mediumDateFormatter =
    DateTimeFormatter.ofLocalizedDateTime(
        FormatStyle.MEDIUM,
        FormatStyle.SHORT)

fun LocalDateTime?.toMediumFormat() =
    this?.format(mediumDateFormatter)

@BindingAdapter("data")
fun setData(recyclerView: RecyclerView, items: List<AdapterItem>?) {
    if (recyclerView.adapter is EventsAdapter && items != null) {
        (recyclerView.adapter as EventsAdapter).submitList(items)
    }else if (recyclerView.adapter is CollaborationsAdapter && items != null){
        (recyclerView.adapter as CollaborationsAdapter).submitList(items)
    }
}