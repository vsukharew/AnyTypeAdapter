package vsukharev.anytypeadapter.adapter

import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import vsukharev.anytypeadapter.common.CoroutineDispatcherRule
import vsukharev.anytypeadapter.common.MockInitializer
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.domain.Track
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import java.util.*

class AnyTypeAdapterTest : MockInitializer() {

    @Test
    fun `onBindViewHolder - call with non-empty payload - verify holder applyPayload get called`() {
        val adapter = AnyTypeAdapter()
        val captor = argumentCaptor<List<Any>>()
        val payload = mutableListOf(Any())
        adapter.onBindViewHolder(trackHolder as AnyTypeViewHolder<Any, ViewBinding>, 0, payload)
        verify(trackHolder).applyPayload(captor.capture())
        assert(payload == captor.firstValue)
    }
}