package vsukharev.anytypeadapter.delegate

import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import vsukharev.anytypeadapter.common.MockInitializer
import vsukharev.anytypeadapter.domain.Track
import vsukharev.anytypeadapter.item.AdapterItem
import kotlin.random.Random

class AnyTypeDelegateTest : MockInitializer() {

    @Test
    fun `delegate's and holder's bind() methods are called with the same object`() {
        val track = Track()
        val captor = argumentCaptor<Track>()
        trackDelegate.bind(
            AdapterItem(
                track.id,
                track,
                Random(System.currentTimeMillis()).nextInt()
            ), trackHolder
        )
        verify(trackHolder).bind(captor.capture())
        assert(track == captor.firstValue)
    }
}