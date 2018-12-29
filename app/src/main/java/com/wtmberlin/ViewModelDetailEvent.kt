package com.wtmberlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelDetailEvent: ViewModel() {
    val event = MutableLiveData<Int>()
}