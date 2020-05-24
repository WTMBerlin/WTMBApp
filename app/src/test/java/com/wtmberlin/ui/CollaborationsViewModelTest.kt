package com.wtmberlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.wtmberlin.SetMainDispatcherRule
import com.wtmberlin.UnconfinedTestScopeRule
import com.wtmberlin.data.Repository
import com.wtmberlin.data.Result
import com.wtmberlin.data.VenueName
import com.wtmberlin.mock
import com.wtmberlin.util.LogException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CollaborationsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var setMainDispatcherRule = SetMainDispatcherRule()
    @get:Rule
    var unconfinedTestScopeRule = UnconfinedTestScopeRule()

    @Mock
    private lateinit var mockRepo: Repository
    @Mock
    private lateinit var mockLogException: LogException

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) { CollaborationsViewModel(mockRepo, mockLogException) }

    private fun startError(exception: Exception = Exception()): Exception {
        mock<Repository> {
            onBlocking {
                mockRepo.venues()
            } doReturn Result(false, null, exception)
        }
        return exception
    }

    private fun startVenue(venueName: VenueName = mock()): List<VenueName> {
        given(venueName.name).willReturn("Google")
        mock<Repository> {
            onBlocking {
                mockRepo.venues()
            } doReturn Result(false, listOf(venueName), null)
        }
        return listOf(venueName)
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        doNothing().`when`(mockLogException).getException(any())
    }

    @Test
    fun `on data load error`() {
        startError()

        viewModel.adapterItems.observeForever { }

        verify(mockLogException, times(1)).getException(any())
    }

    @Test
    fun `on data load success`() {
        val venues = startVenue()

        viewModel.adapterItems.observeForever { }

        val result = viewModel.adapterItems.value
        assertEquals(venues[0].name, result?.get(0)?.id)
        assertEquals(venues[0].name, result?.get(0)?.venueName)
    }

}
