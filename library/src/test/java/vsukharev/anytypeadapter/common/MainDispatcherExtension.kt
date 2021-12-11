package vsukharev.anytypeadapter.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

@ExperimentalCoroutinesApi
class MainDispatcherExtension : BeforeAllCallback, ExtensionContext.Store.CloseableResource {
    private val dispatcher = TestCoroutineDispatcher()

    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun close() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }
}