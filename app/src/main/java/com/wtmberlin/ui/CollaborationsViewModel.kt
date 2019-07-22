package com.wtmberlin.ui

import androidx.lifecycle.MutableLiveData
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.VenueName
import com.wtmberlin.util.CoroutineViewModel
import com.wtmberlin.util.LogException
import kotlinx.coroutines.launch

class CollaborationsViewModel(
    repository: Repository,
    private val logException: LogException
) : CoroutineViewModel() {
    val adapterItems = MutableLiveData<List<CollaborationsAdapterItem>>()

    init {
        launch {
            onDataLoaded(repository.venues())
        }
    }

    private fun onDataLoaded(result: Result<List<VenueName>>) {
        result.data?.let { processVenues(it) }
        result.error?.let { logException.getException(it) }
    }

    private fun processVenues(list: List<VenueName>) {
        adapterItems.value = list.map {
            CollaborationsAdapterItem(it.name, it.name)
        }
    }

}