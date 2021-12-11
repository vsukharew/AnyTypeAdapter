package vsukharev.anytypeadapter.adapter

import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.kotlin.argumentCaptor
import vsukharev.anytypeadapter.common.MainDispatcherExtension
import vsukharev.anytypeadapter.common.MockInitializer
import vsukharev.anytypeadapter.domain.Track
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder

@ExtendWith(MainDispatcherExtension::class)
class AnyTypeAdapterTest : MockInitializer() {

    private val dispatcher = TestCoroutineDispatcher()

    @Test
    fun `onBindViewHolder - call with non-empty payload - verify holder applyPayload get called`() {
        val adapter = AnyTypeAdapter()
        val captor = argumentCaptor<List<Any>>()
        val payload = mutableListOf(Any())
        adapter.onBindViewHolder(trackHolder as AnyTypeViewHolder<Any, ViewBinding>, 0, payload)
        verify(trackHolder).applyPayload(captor.capture())
        assert(payload == captor.firstValue)
    }

    @Test
    fun `onBindViewHolder - call with empty payload - verify holder applyPayload not called`() {
        val adapter = AnyTypeAdapter().apply {
            diffStrategy = DiffStrategy.DiscardLatest(dispatcher)
        }
        AnyTypeCollection.Builder()
            .add(listOf(Track(), Track()), trackDelegate)
            .build()
            .let { adapter.setCollection(it) }
        val payload = mutableListOf<Any>()
        adapter.onBindViewHolder(trackHolder as AnyTypeViewHolder<Any, ViewBinding>, 0, payload)
        verify(trackHolder, times(0)).applyPayload(anyList())
    }

    @Test
    fun `onBindViewHolder - call with overload with no payload - verify holder applyPayload not called`() {
        val adapter = AnyTypeAdapter().apply {
            diffStrategy = DiffStrategy.DiscardLatest(dispatcher)
        }
        AnyTypeCollection.Builder()
            .add(listOf(Track(), Track()), trackDelegate)
            .build()
            .let { adapter.setCollection(it) }
        adapter.onBindViewHolder(trackHolder as AnyTypeViewHolder<Any, ViewBinding>, 0)
        verify(trackHolder, times(0)).applyPayload(anyList())
    }
}