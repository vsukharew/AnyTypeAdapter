package vsukharev.anytypeadapter.delegate

import androidx.viewbinding.ViewBinding
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import vsukharev.anytypeadapter.domain.Track
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.trackDelegate

class AnyTypeDelegateTest {
    private val holder = mock<AnyTypeViewHolder<Track, ViewBinding>>()

    @Test
    fun bind() {
        val track = Track()
        trackDelegate.bind(AdapterItem("", track), holder)
        verify(holder).bind(track)
    }
}