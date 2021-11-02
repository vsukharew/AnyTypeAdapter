package vsukharev.anytypeadapter.delegate

import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import vsukharev.anytypeadapter.common.MockInitializer
import vsukharev.anytypeadapter.domain.Track
import vsukharev.anytypeadapter.item.AdapterItem

class AnyTypeDelegateTest : MockInitializer() {

    @Test
    fun bind_callWithTrack_verifyHolderBindGetCalledWithTheSameTrack() {
        val track = Track()
        val captor = argumentCaptor<Track>()
        trackDelegate.bind(AdapterItem(track.id, track), trackHolder)
        verify(trackHolder).bind(captor.capture())
        assert(track == captor.firstValue)
    }
}