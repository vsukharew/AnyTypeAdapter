package vsukharev.anytypeadapter.adapter

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach

sealed class DiffStrategy(dispatcher: CoroutineDispatcher) {
    protected val scope = CoroutineScope(SupervisorJob() + dispatcher)

    abstract fun calculateDiff(diffBlock: suspend () -> Unit?)

    class Queue(
        private val diffChannel: Channel<suspend () -> Unit?> = Channel(Channel.BUFFERED),
        dispatcher: CoroutineDispatcher = Dispatchers.Default
    ) : DiffStrategy(dispatcher) {

        init {
            scope.launch {
                diffChannel.consumeEach { it.invoke() }
            }
        }

        override fun calculateDiff(diffBlock: suspend () -> Unit?) {
            scope.launch { diffChannel.send(diffBlock) }
        }
    }

    class DiscardLatest(
        dispatcher: CoroutineDispatcher = Dispatchers.Default
    ) : DiffStrategy(dispatcher) {
        var diffJob: Job? = null
        override fun calculateDiff(diffBlock: suspend () -> Unit?) {
            diffJob?.cancel()
            diffJob = scope.launch { diffBlock.invoke() }
        }
    }
}