package vsukharev.anytypeadapter.delegate

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import vsukharev.anytypeadapter.MockInitializer
import vsukharev.anytypeadapter.item.AdapterItem

class AnyTypeDelegateTest : MockInitializer() {

    @Test
    fun bind_callWithAnyData_verifyHolderBindGetCalled() {
        trackDelegate.bind(AdapterItem(any(), any()), trackHolder)
        verify(trackHolder).bind(any())
    }
}