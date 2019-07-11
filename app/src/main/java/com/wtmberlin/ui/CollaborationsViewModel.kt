package com.wtmberlin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.VenueName
import kotlinx.coroutines.launch
import timber.log.Timber

class CollaborationsViewModel(repository: Repository) : ViewModel() {
    val adapterItems = MutableLiveData<List<CollaborationsAdapterItem>>()

    init {
        viewModelScope.launch {
            onDataLoaded(repository.venues())
        }
    }

    private fun onDataLoaded(result: Result<List<VenueName>>) {
        result.data?.let { processVenues(it) }
        result.error?.let { Timber.i(it) }
    }

    private fun processVenues(list: List<VenueName>) {
        adapterItems.value = list.map {
            CollaborationsAdapterItem(it.name, it.name)
        }
    }

}