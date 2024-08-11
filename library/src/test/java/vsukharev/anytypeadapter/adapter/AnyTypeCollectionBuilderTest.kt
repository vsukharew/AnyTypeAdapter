package vsukharev.anytypeadapter.adapter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import vsukharev.anytypeadapter.common.MockInitializer
import vsukharev.anytypeadapter.domain.Activity
import vsukharev.anytypeadapter.domain.Track
import vsukharev.anytypeadapter.item.AdapterItem

class AnyTypeCollectionBuilderTest : MockInitializer() {

    @Test
    fun `itemViewTypes-to-delegates has that size how many different by type items were added`() {
        val times = 10
        AnyTypeCollection.Builder()
            .apply {
                repeat(times) { add(Track(), trackDelegate) }
                add(headerDelegate)
                repeat(times) { add(Activity(), activityDelegate) }
            }
            .build()
            .apply {
                val expectedDifferentViewTypesCount = 3
                val actualDifferentViewTypesCount = itemViewTypesToDelegates.size
                assertEquals(expectedDifferentViewTypesCount, actualDifferentViewTypesCount)
            }
    }

    @Test
    fun `all items are added`() {
        val times = 10
        val allItems = mutableListOf<Any>()
        AnyTypeCollection.Builder()
            .apply {
                repeat(times) {
                    val track = Track()
                    add(track, trackDelegate)
                    allItems.add(track)
                }
                add(headerDelegate).also { allItems.add(Unit) }
                repeat(times) {
                    val activity = Activity()
                    add(activity, activityDelegate)
                    allItems.add(activity)
                }
            }
            .build()
            .apply {
                val expectedItems: List<Any> = allItems
                val actualItems = items.map(AdapterItem<Any>::data)
                assertEquals(expectedItems, actualItems)
            }
    }

    @Test
    fun `getItemViewType() called twice for each delegate except last one added`() {
        val track = Track()
        val activity = Activity()
        AnyTypeCollection.Builder()
            .apply {
                add(track, trackDelegate)
                add(headerDelegate)
                add(activity, activityDelegate) // corner addition

                verify(trackDelegate, times(1)).getItemViewType()
                verify(headerDelegate, times(1)).getItemViewType()
                verify(activityDelegate, times(1)).getItemViewType()
            }
    }

    @Test
    fun `each time when new item is added getItemId() is called`() {
        val times = 10
        AnyTypeCollection.Builder()
            .apply {
                repeat(times) {
                    val track = Track()
                    add(track, trackDelegate)
                    verify(trackDelegate).getItemId(track)
                }
                add(headerDelegate)
                verify(headerDelegate).getItemId(Unit)
                repeat(times) {
                    val activity = Activity()
                    add(activity, activityDelegate)
                    verify(activityDelegate).getItemId(activity)
                }
            }
    }

    @Test
    fun `each time item is added getItemViewType() is called`() {
        val track = Track()
        val activity = Activity()
        AnyTypeCollection.Builder()
            .apply {
                add(track, trackDelegate)
                add(headerDelegate)
                add(activity, activityDelegate)

                verify(trackDelegate, times(1)).getItemViewType()
                verify(headerDelegate, times(1)).getItemViewType()
                verify(activityDelegate, times(1)).getItemViewType()
            }
    }

    @Test
    fun `each time list item is added getItemId() is called`() {
        val tracksList = listOf(Track(), Track())
        val captor = argumentCaptor<Track>()
        AnyTypeCollection.Builder()
            .apply {
                add(tracksList, trackDelegate)
                verify(trackDelegate, times(tracksList.size)).getItemId(captor.capture())
                val expectedArguments: List<Track> = tracksList
                val actualArguments = captor.allValues
                assertEquals(expectedArguments, actualArguments)
            }
    }

    @Test
    fun `single item is added only when predicate of addIf() returns true`() {
        val times = 10
        AnyTypeCollection.Builder()
            .apply {
                repeat(times) {
                    addIf(Track(), trackDelegate) { it % 2 == 0 }
                }
            }
            .build()
            .apply {
                val expectedItemsCount = times / 2
                val actualItemsCount = size
                assertEquals(expectedItemsCount, actualItemsCount)
            }
    }

    @Test
    fun `list is added only when predicate of addIf() returns true`() {
        val times = 10
        AnyTypeCollection.Builder()
            .apply {
                repeat(times) {
                    addIf(listOf(Track()), trackDelegate) { it % 2 == 0 }
                }
            }
            .build()
            .apply {
                val expectedItemsCount = times / 2
                val actualItemsCount = size
                assertEquals(expectedItemsCount, actualItemsCount)
            }
    }

    @Test
    fun `no-data-item is added only when predicate of addIf() returns true`() {
        val times = 10
        AnyTypeCollection.Builder()
            .apply {
                repeat(times) {
                    addIf(headerDelegate) { it % 2 == 0 }
                }
            }
            .build()
            .apply {
                val expectedItemsCount = times / 2
                val actualItemsCount = size
                assertEquals(expectedItemsCount, actualItemsCount)
            }
    }

    @Test
    fun `when predicate of addIf returns true getItemId() is called`() {
        val times = 10
        val tracks = buildList {
            repeat(times) {
                add(Track())
            }
        }
        val captor = argumentCaptor<Track>()
        AnyTypeCollection.Builder()
            .apply {
                tracks.forEachIndexed { index, track ->
                    addIf(track, trackDelegate) { index % 2 == 0 }
                }
            }
            .build()
            .apply {
                verify(trackDelegate, times(size)).getItemId(captor.capture())
                assertTrue(captor.allValues.all { it in tracks })
            }
    }

    @Test
    fun `empty lists should not be added`() {
        AnyTypeCollection.Builder()
            .addIfNotEmpty(emptyList(), trackDelegate)
            .addIfNotEmpty(emptyList<Track>(), trackListDelegate)
            .build()
            .apply { assert(itemViewTypesToDelegates.isEmpty() && items.isEmpty()) }
    }

    @Test
    fun `if empty list is added delegate methods should not be called`() {
        AnyTypeCollection.Builder()
            .addIfNotEmpty(emptyList(), trackDelegate)
            .addIfNotEmpty(emptyList<Track>(), trackListDelegate)
            .apply {
                verifyNoInteractions(trackDelegate)
                verifyNoInteractions(trackListDelegate)
            }
    }

    @Test
    fun `non empty lists should be added`() {
        AnyTypeCollection.Builder()
            .addIfNotEmpty(listOf(Track()), trackDelegate)
            .addIfNotEmpty(listOf(Track()), trackListDelegate)
            .build()
            .apply { assert(itemViewTypesToDelegates.isNotEmpty() && items.isNotEmpty()) }
    }

    @Test
    fun `if non empty list is added delegate methods should be called`() {
        val tracks = listOf(Track())
        val trackCaptor = argumentCaptor<Track>()
        val tracksListCaptor = argumentCaptor<List<Track>>()
        AnyTypeCollection.Builder()
            .addIfNotEmpty(tracks, trackDelegate)
            .addIfNotEmpty(tracks, trackListDelegate)
            .apply {
                verify(trackDelegate).getItemId(trackCaptor.capture())
                verify(trackDelegate).getItemViewType()
                val expectedArgument: List<Track> = tracks
                val actualArgument = trackCaptor.allValues
                assertEquals(expectedArgument, actualArgument)
            }.apply {
                verify(trackListDelegate).getItemId(tracksListCaptor.capture())
                verify(trackListDelegate).getItemViewType()
                val expectedArgument: List<Track> = tracks
                val actualArgument = tracksListCaptor.firstValue
                assertEquals(expectedArgument, actualArgument)
            }
    }

    @Test
    fun `empty collection has empty itemViewType-to-delegate map`() {
        val emptyCollection = AnyTypeCollection.EMPTY
        assertTrue(emptyCollection.itemViewTypesToDelegates.isEmpty())
    }
}