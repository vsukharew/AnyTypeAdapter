package vsukharev.anytypeadapter

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import vsukharev.anytypeadapter.adapter.AnyTypeCollection
import vsukharev.anytypeadapter.domain.Activity
import vsukharev.anytypeadapter.domain.Track

class AnyTypeCollectionBuilderTest : MockInitializer() {

    @Test
    fun add_addItemsOfDifferentViewTypes_eachTimeViewTypeIsDifferentNewItemIsAddedToItemsMetaDataCollection() {
        var itemsCount = 0
        var itemsMetadataCount = 0
        AnyTypeCollection.Builder()
            .apply {
                repeat((1..10).count()) {
                    add(Track(), trackDelegate).also { itemsCount++ }
                }.also { itemsMetadataCount++ }
                add(headerDelegate).also { itemsCount++; itemsMetadataCount++ }
                repeat((1..10).count()) {
                    add(Activity(), activityDelegate).also { itemsCount++ }
                }.also { itemsMetadataCount++ }
            }
            .build()
            .apply {
                assert(itemsMetaData.size == itemsMetadataCount && items.size == itemsCount)
            }
    }

    @Test
    fun add_addItemsOfDifferentViewTypes_verifyGetItemIdCalledEachTimeWhenItemAdded() {
        val tracksCount = 10
        val activitiesCount = 10
        AnyTypeCollection.Builder()
            .apply {
                repeat((1..tracksCount).count()) {
                    add(Track(), trackDelegate)
                }
                add(headerDelegate)
                repeat((1..activitiesCount).count()) {
                    add(Activity(), activityDelegate)
                }

                verify(trackDelegate, times(tracksCount)).getItemId(any())
                verify(headerDelegate, times(1)).getItemId(any())
                verify(activityDelegate, times(activitiesCount)).getItemId(any())
            }
    }

    @Test
    fun add_addItemsOfDifferentViewTypes_verifyGetItemViewTypeCalledTwiceEachTimeItemAddedExceptCornerAdditions() {
        val tracksCount = 10
        val tracksListsCount = 1
        val activitiesCount = 10
        AnyTypeCollection.Builder()
            .apply {
                repeat((1..tracksCount).count()) {
                    add(Track(), trackDelegate)
                } //corner addition
                add(headerDelegate)
                add(listOf(Track(), Track()), trackListDelegate)
                repeat((1..activitiesCount).count()) {
                    add(Activity(), activityDelegate)
                } //corner addition

                verify(trackDelegate, times((tracksCount * 2 - 1))).getItemViewType()
                verify(headerDelegate, times(2)).getItemViewType()
                verify(trackListDelegate, times(tracksListsCount * 2)).getItemViewType()
                verify(activityDelegate, times((activitiesCount * 2 - 1))).getItemViewType()
            }
    }

    @Test
    fun addIf_predicateIsEitherTrueEitherFalse_itemIsAddedOnlyWhenPredicateIsTrue() {
        var itemsCount = 0
        var itemsMetadataCount = 0
        AnyTypeCollection.Builder()
            .apply {
                repeat((1..10).count()) {
                    (addIf(Track(), trackDelegate) { it % 2 == 0 }).also { itemsCount++ }
                }.also { itemsMetadataCount++ }
            }
            .build()
            .apply {
                assert(itemsMetaData.size == itemsMetadataCount && size == itemsCount / 2)
            }
    }

    @Test
    fun addIf_predicateIsEitherTrueEitherFalse_noDataItemIsAddedOnlyWhenPredicateIsTrue() {
        var itemsCount = 0
        var itemsMetadataCount = 0
        AnyTypeCollection.Builder()
            .addIf(headerDelegate) { true }.also { itemsCount++; itemsMetadataCount++ }
            .addIf(headerDelegate) { false }.also { itemsCount++ }
            .build()
            .apply { assert(itemsMetaData.size == itemsMetadataCount && size != itemsCount && size == 1) }
    }

    @Test
    fun addIf_dataIsListAndPredicateIsEitherTrueEitherFalse_itemIsAddedOnlyWhenPredicateIsTrue() {
        var itemsCount = 0
        var itemsMetadataCount = 0
        AnyTypeCollection.Builder()
            .apply {
                repeat((1..10).count()) {
                    (addIf(listOf(Track()), trackDelegate) { it % 2 == 0 }).also { itemsCount++ }
                }.also { itemsMetadataCount++ }
            }
            .build()
            .apply {
                assert(itemsMetaData.size == itemsMetadataCount && items.size == itemsCount / 2)
            }
    }

    @Test
    fun addIf_predicateIsEitherTrueEitherFalse_verifyGetItemIdCalledOnlyWhenPredicateIsTrue() {
        var itemsCount = 0
        var itemsMetadataCount = 0
        AnyTypeCollection.Builder()
            .apply {
                repeat((1..10).count()) {
                    (addIf(Track(), trackDelegate) { it % 2 == 0 }).also { itemsCount++ }
                }.also { itemsMetadataCount++ }

                verify(trackDelegate, times((itemsCount / 2))).getItemId(any())
            }
    }

    @Test
    fun addIfNotEmpty_inputListIsEmpty_dataListShouldNotBeAdded() {
        AnyTypeCollection.Builder()
            .addIfNotEmpty(listOf(), trackDelegate)
            .addIfNotEmpty(listOf<Track>(), trackListDelegate)
            .build()
            .apply { assert(itemsMetaData.isEmpty() && items.isEmpty()) }
    }

    @Test
    fun addIfNotEmpty_inputListIsEmpty_noneDelegateMethodsGetCalled() {
        AnyTypeCollection.Builder()
            .addIfNotEmpty(listOf(), trackDelegate)
            .addIfNotEmpty(listOf<Track>(), trackListDelegate)
            .apply {
                verifyZeroInteractions(trackDelegate)
                verifyZeroInteractions(trackListDelegate)
            }
    }

    @Test
    fun addIfNotEmpty_inputListIsNotEmpty_dataListShouldBeAdded() {
        AnyTypeCollection.Builder()
            .addIfNotEmpty(listOf(Track()), trackDelegate)
            .addIfNotEmpty(listOf(Track()), trackListDelegate)
            .build()
            .apply { assert(itemsMetaData.isNotEmpty() && items.isNotEmpty()) }
    }

    @Test
    fun addIfNotEmpty_inputListIsNotEmpty_delegateMethodsGetCalled() {
        AnyTypeCollection.Builder()
            .addIfNotEmpty(listOf(Track()), trackDelegate)
            .addIfNotEmpty(listOf(Track()), trackListDelegate)
            .apply {
                verify(trackDelegate).getItemId(any())
                verify(trackListDelegate).getItemId(any())
            }
    }

    @Test
    fun build_emptyCollection_shouldGetEmptyCollectionOfRanges() {
        val collection = AnyTypeCollection.EMPTY
        val expected = emptyList<IntRange>()
        val actual = collection.positionsRanges
        assert(expected == actual)
    }
}