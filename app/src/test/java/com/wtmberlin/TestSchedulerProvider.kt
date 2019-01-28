package com.wtmberlin

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider : SchedulerProvider {
    override val computation: Scheduler = Schedulers.trampoline()
    override val io: Scheduler = Schedulers.trampoline()
    override val ui: Scheduler = Schedulers.trampoline()
}