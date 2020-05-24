package com.wtmberlin.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

interface AdapterItem {
    val id: Any
    val viewType: Int
}

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AdapterItem>() {
    override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem) =
        oldItem.id == newItem.id && oldItem.viewType == newItem.viewType

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem) =
        oldItem == newItem
}
