package vsukharev.anytypeadapter.adapter

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class DiffStrategyTest {
    private val diffChannelMock = mock<Channel<suspend () -> Unit?>>().apply {
        stub {
            onBlocking { send(any()) }.doReturn(Unit)
        }
    }
    private val jobMock = mock<Job>()

    @Test
    fun `calculateDiff QueueStrategy Verify that diffBlock is sent to channel`() {
        val captor = argumentCaptor<suspend () -> Unit?>()
        val strategy = DiffStrategy.Queue(diffChannelMock) // run the first coroutine
        val diffBlock: suspend () -> Unit? = { }
        runTest {
            strategy.calculateDiff(diffBlock)
            // run the second one
            // they're unable to run in parallel inside runTest { ... }
            verify(diffChannelMock).send(captor.capture())
            assert(diffBlock == captor.firstValue)
        }
    }

    @Test
    fun `calculateDiff DiscardLatest verify previous diffJob cancelled`() {
        val strategy = DiffStrategy.DiscardLatest().apply { diffJob = jobMock }
        val diffBlock = suspend { }
        strategy.calculateDiff(diffBlock)
        verify(jobMock).cancel()
    }

    @Test
    fun `calculateDiff DiscardLatest verify diffBlock called`() {
        val diffBlock = mock<DiffBlock>().apply {
            `when`(invoke()).thenReturn { }
        }
        val strategy = DiffStrategy.DiscardLatest()
        strategy.calculateDiff(diffBlock())
        verify(diffBlock).invoke()
    }

    private fun interface DiffBlock : () -> suspend () -> Unit?
}