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
}