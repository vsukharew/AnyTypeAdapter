package vsukharev.anytypeadapter.adapter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import vsukharev.anytypeadapter.common.MockInitializer
import vsukharev.anytypeadapter.domain.Activity
import vsukharev.anytypeadapter.domain.Track

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
    fun currentItemViewTypePosition_collectionIsEmpty_shouldBeEqualToNoPosition() {
        val collection = AnyTypeCollection.EMPTY
        assert(collection.currentItemViewTypePosition == AnyTypeCollection.NO_POSITION)
    }

    @Test
    fun findCurrentItemViewType_iterateThroughCollection_whileItemsAreOfSameFindCurrentItemViewTypePositionReturnsSavedValue() {
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
                        currentItemViewTypePosition = position // in real code, this saving takes place in adapter
                    }
                }
                assert(currentItemViewTypePosition == itemsMetaData.size - 1)
            }
    }
}