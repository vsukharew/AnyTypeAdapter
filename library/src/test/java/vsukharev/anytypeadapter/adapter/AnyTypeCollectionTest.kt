package vsukharev.anytypeadapter.adapter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import vsukharev.anytypeadapter.common.MockInitializer
import vsukharev.anytypeadapter.domain.Activity
import vsukharev.anytypeadapter.domain.Track
import vsukharev.anytypeadapter.item.AdapterItem

class AnyTypeCollectionTest : MockInitializer() {

    @Test
    fun currentItemViewTypeDelegate_collectionIsEmpty_exceptionShouldBeThrown() {
        val collection = AnyTypeCollection.EMPTY
        assertThrows<IllegalStateException> { collection.currentItemViewTypeDelegate }
    }

    @Test
    fun currentItemViewTypeDelegate_collectionIsNotEmpty_exceptionShouldNotBeThrown() {
        AnyTypeCollection.Builder()
            .add(headerDelegate)
            .build()
            .apply {
                assertDoesNotThrow { currentItemViewTypeDelegate }
                assert(currentItemViewTypeDelegate::class == headerDelegate::class)
            }
    }

    @Test
    fun `unable to access currentItemViewTypeDelegate in an empty AnyTypeCollection`() {
        val collection = AnyTypeCollection.EMPTY
        assertThrows<IllegalStateException> { collection.currentItemViewTypeDelegate }
    }
}