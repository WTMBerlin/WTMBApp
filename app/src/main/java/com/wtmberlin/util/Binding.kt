package com.wtmberlin.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wtmberlin.CollaborationsAdapter
import com.wtmberlin.EventsAdapter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

private val mediumDateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)

private val longDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)

private val shortTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

fun LocalDateTime?.toMediumFormat() =
    this?.format(mediumDateFormatter)

fun LocalDateTime?.toLongDate() =
    this?.format(longDateFormatter)

fun String?.toHtml() =
    if (this != null) HtmlCompat.fromHtml(this) else null

fun fromTimeToTime(start: LocalDateTime?, end: LocalDateTime?): String? {
    if (start == null || end == null) {
        return null
    }

    return "${start.format(shortTimeFormatter)} to ${end.format(shortTimeFormatter)}"
}

@BindingAdapter("data")
fun setData(recyclerView: RecyclerView, items: List<AdapterItem>?) {
    if (recyclerView.adapter is EventsAdapter && items != null) {
        (recyclerView.adapter as EventsAdapter).submitList(items)
    } else if (recyclerView.adapter is CollaborationsAdapter && items != null) {
        (recyclerView.adapter as CollaborationsAdapter).submitList(items)
    }
}

@BindingAdapter(value = ["image_url", "placeholder"], requireAll = false)
fun setImageUrl(imageView: ImageView, image_url: String?, placeholder: Drawable?) {
    if (image_url == null) {
        imageView.setImageDrawable(placeholder)
    } else {
        val requestCreator = Picasso.get().load(image_url)

        placeholder?.let { requestCreator.placeholder(it) }

        requestCreator.into(imageView)
    }
}