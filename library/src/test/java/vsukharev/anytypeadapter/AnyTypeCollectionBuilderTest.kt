package vsukharev.anytypeadapter

import org.junit.jupiter.api.Test
import vsukharev.anytypeadapter.adapter.AnyTypeCollection
import vsukharev.anytypeadapter.domain.Activity
import vsukharev.anytypeadapter.domain.Track

class AnyTypeCollectionBuilderTest {

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
            .addIf(headerDelegate) { true }.also { itemsCount++ ; itemsMetadataCount++ }
            .addIf(headerDelegate) { false }.also { itemsCount++}
            .build()
            .apply { itemsMetaData.size == 1 && size == 1 }
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
    fun addIfNotEmpty_inputListIsEmpty_dataListShouldNotBeAdded() {
        AnyTypeCollection.Builder()
            .addIfNotEmpty(listOf(), trackDelegate)
            .addIfNotEmpty(listOf<Track>(), trackListDelegate)
            .build()
            .apply { assert(itemsMetaData.isEmpty() && items.isEmpty()) }
    }

    @Test
    fun addIfNotEmpty_inputListIsNotEmpty_dataListShouldBeAdded() {
        AnyTypeCollection.Builder()
            .addIfNotEmpty(listOf(Track()), trackDelegate)
            .addIfNotEmpty(listOf(Track()), trackListDelegate)
            .build()
            .apply { assert(itemsMetaData.isNotEmpty() && items.isNotEmpty()) }
    }
}