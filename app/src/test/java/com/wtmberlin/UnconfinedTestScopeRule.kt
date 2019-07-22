package com.wtmberlin

import com.wtmberlin.util.background
import com.wtmberlin.util.io
import com.wtmberlin.util.ui
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class UnconfinedTestScopeRule : TestWatcher() {

    override fun starting(description: Description?) {
        ui = Dispatchers.Unconfined
        io = Dispatchers.Unconfined
        background = Dispatchers.Unconfined
    }
}