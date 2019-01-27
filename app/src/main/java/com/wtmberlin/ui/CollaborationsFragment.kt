package com.wtmberlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import com.wtmberlin.R
import com.wtmberlin.databinding.CollaborationsScreenBinding
import com.wtmberlin.util.AdapterItem
import com.wtmberlin.util.BindingViewHolder
import com.wtmberlin.util.DIFF_CALLBACK
import kotlinx.android.synthetic.main.collaborations_screen.*
import org.koin.android.viewmodel.ext.android.viewModel

class CollaborationsFragment : Fragment() {

    private val viewModel: CollaborationsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil
                .inflate<CollaborationsScreenBinding>(
                    inflater,
                    R.layout.collaborations_screen,
                    container,
                    false
                )

        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collaborations_recycler.adapter = CollaborationsAdapter()
    }
}

class CollaborationsAdapter : ListAdapter<AdapterItem, BindingViewHolder>(DIFF_CALLBACK) {
    override fun getItemViewType(position: Int) = getItem(position).viewType
    override fun onCreateViewHolder(
        parent: ViewGroup,
        @LayoutRes viewType: Int
    ): BindingViewHolder {
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
        holder.bind(getItem(position))
    }
}


class CollaborationsAdapterItem(override val id: String, val venueName: String) : AdapterItem {
    override val viewType = R.layout.collaborations_event_item
}