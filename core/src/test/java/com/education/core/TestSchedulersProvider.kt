package com.education.core

import com.education.core_api.extension.SchedulersProvider
import io.reactivex.Scheduler

class TestSchedulersProvider (
    private val scheduler: Scheduler
) : SchedulersProvider
{
    override fun io(): Scheduler = scheduler

    override fun mainThread(): Scheduler = scheduler

    override fun computation(): Scheduler = scheduler
}