package com.wtmberlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailEventViewModel: ViewModel() {
    val event = MutableLiveData<Int>()
}