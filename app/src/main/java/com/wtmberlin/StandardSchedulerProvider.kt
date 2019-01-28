package com.wtmberlin

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StandardSchedulerProvider : SchedulerProvider {
    override val computation: Scheduler = Schedulers.computation()
    override val io: Scheduler = Schedulers.io()
    override val ui: Scheduler = AndroidSchedulers.mainThread()
}