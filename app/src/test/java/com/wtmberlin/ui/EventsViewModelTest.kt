package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.wtmberlin.TestSchedulerProvider
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.WtmEvent
import com.wtmberlin.mock
import com.wtmberlin.testObserver
import io.reactivex.processors.PublishProcessor
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.threeten.bp.ZonedDateTime

class EventsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val mockEvents = PublishProcessor.create<Result<List<WtmEvent>>>()
    val mockRepository = mock<Repository>().also {
        `when`(it.events()).thenReturn(mockEvents)
    }

    val viewModel = EventsViewModel(mockRepository, TestSchedulerProvider())

    @Test
    fun `when events are refreshed emits refresh true`() {
        val refreshObserver = viewModel.refreshing.testObserver()

        mockEvents.onNext(Result(loading = true, data = null, error = null))

        assertThat(refreshObserver.observedValues)
            .containsExactly(true)
    }

    @Test
    fun `when events are refreshed emits refresh false`() {
        val refreshObserver = viewModel.refreshing.testObserver()

        mockEvents.onNext(Result(loading = false, data = null, error = null))

        assertThat(refreshObserver.observedValues)
            .containsExactly(false)
    }

    @Test
    fun `when data emits events`() {
        val refreshObserver = viewModel.refreshing.testObserver()
        val dataOld: MutableList<WtmEvent> = mutableListOf<WtmEvent>()
        dataOld.add(0,element = WtmEvent("1","Android Co-Learning Testing part 1", TODO(),TODO(),"amazing event","fake url",TODO()))
        dataOld.add(1,element = WtmEvent("2","Android Co-Learning Testing part 2", TODO(),TODO(),"amazing event","fake url",TODO()))
        dataOld.add(2,element = WtmEvent("3","Android Co-Learning Testing part 3", TODO(),TODO(),"amazing event","fake url",TODO()))
        dataOld.add(3,element = WtmEvent("4","Android Co-Learning Testing part 4", TODO(),TODO(),"amazing event","fake url",TODO()))

        mockEvents.onNext(Result(loading = false, data = dataOld, error = null))
        assertThat(TODO())
            .containsExactly(TODO())
    }
}