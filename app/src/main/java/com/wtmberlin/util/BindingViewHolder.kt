package com.wtmberlin.util

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.wtmberlin.BR

class BindingViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Any, callbacks: Any? = null) {
        binding.setVariable(BR.callbacks, callbacks)
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}