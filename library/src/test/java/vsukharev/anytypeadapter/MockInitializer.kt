package vsukharev.anytypeadapter

import androidx.viewbinding.ViewBinding
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.delegate.NoDataDelegate
import vsukharev.anytypeadapter.domain.Activity
import vsukharev.anytypeadapter.domain.Track
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import java.util.*

open class MockInitializer {
    protected val headerDelegate = mock<NoDataDelegate<ViewBinding>>()
    protected val trackDelegate =
        mock<AnyTypeDelegate<Track, ViewBinding, AnyTypeViewHolder<Track, ViewBinding>>>()
    protected val activityDelegate =
        mock<AnyTypeDelegate<Activity, ViewBinding, AnyTypeViewHolder<Activity, ViewBinding>>>()
    protected val trackListDelegate =
        mock<AnyTypeDelegate<List<Track>, ViewBinding, AnyTypeViewHolder<List<Track>, ViewBinding>>>()

    @BeforeEach
    protected fun initMocks() {
        Mockito.`when`(headerDelegate.getItemViewType()).thenReturn(0)
        Mockito.`when`(headerDelegate.getItemId(any())).thenReturn(UUID.randomUUID().toString())


        Mockito.`when`(trackDelegate.getItemViewType()).thenReturn(1)
        Mockito.`when`(trackDelegate.getItemId(any())).thenReturn(UUID.randomUUID().toString())

        Mockito.`when`(activityDelegate.getItemViewType()).thenReturn(2)
        Mockito.`when`(activityDelegate.getItemId(any())).thenReturn(UUID.randomUUID().toString())

        Mockito.`when`(trackListDelegate.getItemViewType()).thenReturn(3)
        Mockito.`when`(trackListDelegate.getItemId(any())).thenReturn(UUID.randomUUID().toString())
    }
}