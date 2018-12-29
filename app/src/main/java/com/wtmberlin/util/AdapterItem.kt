package com.wtmberlin.util

import androidx.recyclerview.widget.DiffUtil
import com.wtmberlin.EventsAdapterItem

interface AdapterItem {
    val id: Any
    val viewType: Int
}

val DIFF_CALLBACK = object: DiffUtil.ItemCallback<AdapterItem>() {
    override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem) =
        oldItem.id == newItem.id && oldItem.viewType == newItem.viewType

    override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem) =
        oldItem == newItem
}