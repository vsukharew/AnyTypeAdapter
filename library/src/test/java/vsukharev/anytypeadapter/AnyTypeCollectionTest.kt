package vsukharev.anytypeadapter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import vsukharev.anytypeadapter.adapter.AnyTypeCollection
import vsukharev.anytypeadapter.domain.Activity
import vsukharev.anytypeadapter.domain.Track
import java.lang.IllegalStateException

class AnyTypeCollectionTest {

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
    fun currentItemViewTypePosition_collectionIsEmpty_shouldBeEqualToNoPosition() {
        val collection = AnyTypeCollection.EMPTY
        assert(collection.currentItemViewTypePosition == AnyTypeCollection.NO_POSITION)
    }

    @Test
    fun findCurrentItemViewType() {
        AnyTypeCollection.Builder()
            .add(listOf(Track(), Track(), Track()), trackDelegate)
            .add(headerDelegate)
            .add(listOf(Activity(), Activity()), activityDelegate)
            .build()
            .apply {
                for (i in 1 until size) {
                    val position = findCurrentItemViewTypePosition(i)
                    if (items[i].data::class == items[i - 1].data::class) {
                        assert(position == currentItemViewTypePosition)
                    } else {
                        assert(position != currentItemViewTypePosition)
                        currentItemViewTypePosition = position
                    }
                }
                assert(currentItemViewTypePosition == itemsMetaData.size - 1)
            }
    }
}