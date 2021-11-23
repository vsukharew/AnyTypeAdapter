package vsukharev.anytypeadapter.adapter

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.Rule
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*
import vsukharev.anytypeadapter.common.CoroutineDispatcherRule

@ExperimentalCoroutinesApi
class DiffStrategyTest {
    @Rule
    private val coroutineTestRule = CoroutineDispatcherRule()
    private val dispatcher = coroutineTestRule.dispatcher
    private val diffChannelMock = mock<Channel<suspend () -> Unit?>>().apply {
        stub {
            onBlocking { send(any()) }.doReturn(Unit)
        }
    }
    private val jobMock = mock<Job>()

    @Test
    fun `calculateDiff QueueStrategy Verify that diffBlock is sent to channel`() {
        dispatcher.runBlockingTest {
            val captor = argumentCaptor<suspend () -> Unit?>()
            val strategy = DiffStrategy.Queue(diffChannelMock, dispatcher)
            val diffBlock: suspend () -> Unit? = { }
            strategy.calculateDiff(diffBlock)
            verify(diffChannelMock).send(captor.capture())
            assert(diffBlock == captor.firstValue)
        }
    }

    @Test
    fun `calculateDiff DiscardLatest verify previous diffJob cancelled`() {
        dispatcher.runBlockingTest {
            val strategy = DiffStrategy.DiscardLatest().apply { diffJob = jobMock }
            val diffBlock = suspend { }
            strategy.calculateDiff(diffBlock)
            verify(jobMock).cancel()
        }
    }

    @Test
    fun `calculateDiff DiscardLatest verify diffBlock called`() {
        dispatcher.runBlockingTest {
            val diffBlock = mock<DiffBlock>().apply {
                `when`(invoke()).thenReturn {  }
            }
            val strategy = DiffStrategy.DiscardLatest()
            strategy.calculateDiff(diffBlock())
            verify(diffBlock).invoke()
        }
    }

    private fun interface DiffBlock : () -> suspend () -> Unit?
}