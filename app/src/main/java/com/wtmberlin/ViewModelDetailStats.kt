package com.wtmberlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelDetailStats: ViewModel() {
    val stats = MutableLiveData<Int>()
}