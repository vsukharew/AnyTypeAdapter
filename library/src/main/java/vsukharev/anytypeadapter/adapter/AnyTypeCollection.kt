package vsukharev.anytypeadapter.adapter

import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.item.AdapterItemMetaData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.delegate.NoDataDelegate
import java.lang.IllegalStateException

/**
 * Class that wraps items for [AnyTypeAdapter]
 * @property items items to display in [AnyTypeAdapter]
 * @property itemsMetaData metadata for items to display. Helps to determine which data at which position should be bound
 * @property positionsRanges Each two adjacent values in the list represent the positions range
 * in which the items with the same viewType are placed
 */
class AnyTypeCollection private constructor(
    val items: List<AdapterItem<Any>>,
    val itemsMetaData: List<AdapterItemMetaData<Any, ViewBinding>>,
    val positionsRanges: List<IntRange>
) {
    /**
     * Saved position value provided in [RecyclerView.Adapter.getItemViewType]
     */
    internal var currentItemViewTypePosition: Int = NO_POSITION

    /**
     * Returns delegate at the given position in the [itemsMetaData] collection
     */
    val currentItemViewTypeDelegate: AnyTypeDelegate<Any, ViewBinding, AnyTypeViewHolder<Any, ViewBinding>>
        get() {
            return if (itemsMetaData.isNotEmpty()) {
                itemsMetaData[currentItemViewTypePosition].delegate
            } else {
                throw IllegalStateException("Unable to get a delegate in an empty collection")
            }
        }

    val size: Int = items.size

    /**
     * Finds position for the current item view type given current [adapterPosition]
     * @see [AnyTypeCollection.itemsMetaData]
     */
    fun findCurrentItemViewTypePosition(adapterPosition: Int): Int {
        val currentPositionsRange = positionsRanges.getOrNull(currentItemViewTypePosition)
        return if (currentPositionsRange?.contains(adapterPosition) == true) {
            currentItemViewTypePosition
        } else {
            with(positionsRanges) {
                binarySearch {
                    when {
                        adapterPosition in it -> 0
                        adapterPosition < it.first -> 1
                        else -> -1
                    }
                }
            }
        }
    }

    class Builder {
        private val items = mutableListOf<AdapterItem<Any>>()
        private val itemsMetaData = mutableListOf<AdapterItemMetaData<Any, ViewBinding>>()

        /**
         * Adds the single [item] and the corresponding [delegate]
         */
        fun <T : Any, V : ViewBinding, H : AnyTypeViewHolder<T, V>> add(
            item: T,
            delegate: AnyTypeDelegate<T, V, H>
        ): Builder {
            return apply {
                val isCurrentViewTypeEqualToLastAdded = with(itemsMetaData) {
                    isNotEmpty() && get(lastIndex).delegate.getItemViewType() == delegate.getItemViewType()
                }

                // Put new view type only if it isn't equal to the previous one
                when {
                    !isCurrentViewTypeEqualToLastAdded -> {
                        /**
                         * Cast is safe because you don't need to know the exact type later
                         * and [AnyTypeCollection.itemsMetaData] as well as [AnyTypeCollection.items] are immutable
                         * so you won't be able to write the wrong value without recreating [AnyTypeCollection]
                         */
                        @Suppress("UNCHECKED_CAST")
                        itemsMetaData.add(
                            AdapterItemMetaData(
                                items.size,
                                delegate as AnyTypeDelegate<Any, ViewBinding, AnyTypeViewHolder<Any, ViewBinding>>
                            )
                        )
                    }
                    else -> {
                        /* do nothing */
                    }
                }
                items.add(AdapterItem(delegate.getItemId(item), item))
            }
        }

        /**
         * Adds [items] list and the corresponding [delegate]
         */
        fun <T : Any, V : ViewBinding, H : AnyTypeViewHolder<T, V>> add(
            items: List<T>,
            delegate: AnyTypeDelegate<T, V, H>
        ): Builder {
            return apply { items.forEach { add(it, delegate) } }
        }

        /**
         * Adds section without data to bind
         */
        fun <V : ViewBinding> add(delegate: NoDataDelegate<V>): Builder {
            return apply { add(Unit, delegate) }
        }

        /**
         * Adds [item] and the corresponding [delegate] only if [predicate] is true
         * @param predicate the condition determining whether the items and delegate should be added
         */
        fun <T : Any, V : ViewBinding, H : AnyTypeViewHolder<T, V>> addIf(
            item: T,
            delegate: AnyTypeDelegate<T, V, H>,
            predicate: () -> Boolean
        ): Builder {
            return apply {
                if (predicate.invoke()) {
                    add(item, delegate)
                }
            }
        }

        /**
         * Adds [items] and the corresponding [delegate] only if [predicate] is true
         * @param predicate the condition determining whether the items and delegate should be added
         */
        fun <T : Any, V : ViewBinding, H : AnyTypeViewHolder<T, V>> addIf(
            items: List<T>,
            delegate: AnyTypeDelegate<T, V, H>,
            predicate: () -> Boolean
        ): Builder {
            return apply {
                if (predicate.invoke()) {
                    add(items, delegate)
                }
            }
        }

        /**
         * Adds section without data to bind only if [predicate] is true
         * @param predicate the condition determining whether the section should be added
         */
        fun <V : ViewBinding> addIf(
            delegate: NoDataDelegate<V>,
            predicate: () -> Boolean
        ): Builder {
            return apply {
                if (predicate.invoke()) {
                    add(Unit, delegate)
                }
            }
        }

        /**
         * Adds [item] and the corresponding [delegate] only if the item (which must be a collection) is not empty
         */
        fun <T : Iterable<*>, V : ViewBinding, H : AnyTypeViewHolder<T, V>> addIfNotEmpty(
            item: T,
            delegate: AnyTypeDelegate<T, V, H>
        ): Builder {
            return apply { addIf(item, delegate) { item.count() > 0 } }
        }

        /**
         * Adds [items] and the corresponding [delegate] only if the list of items is not empty
         */
        fun <T : Any, V : ViewBinding, H : AnyTypeViewHolder<T, V>> addIfNotEmpty(
            items: List<T>,
            delegate: AnyTypeDelegate<T, V, H>
        ): Builder {
            return apply { addIf(items, delegate) { items.isNotEmpty() } }
        }

        fun build(): AnyTypeCollection {
            val positionsRanges = with(itemsMetaData) {
                when {
                    isEmpty() -> emptyList()
                    else -> {
                        zipWithNext { first, second ->
                            first.position until second.position
                        } + listOf(last().position until items.size)
                    }
                }
            }
            return AnyTypeCollection(items, itemsMetaData, positionsRanges).apply {
                if (items.isNotEmpty()) {
                    currentItemViewTypePosition = 0
                }
            }
        }
    }

    companion object {
        internal const val NO_POSITION = -1
        val EMPTY = Builder().build()
    }
}