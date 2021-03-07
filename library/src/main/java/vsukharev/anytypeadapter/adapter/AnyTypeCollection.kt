package vsukharev.anytypeadapter.adapter

import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.item.AdapterItemMetaData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.delegate.NoDataDelegate

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
    var currentItemViewTypePosition: Int = 0

    /**
     * Returns delegate at the given position in the [itemsMetaData] collection
     */
    val currentItemViewTypeDelegate: AnyTypeDelegate<Any, ViewBinding, AnyTypeViewHolder<Any, ViewBinding>>
        get() = itemsMetaData[currentItemViewTypePosition].delegate

    val size: Int = items.size

    class Builder {
        private val items = mutableListOf<AdapterItem<Any>>()
        private val itemsMetaData = mutableListOf<AdapterItemMetaData<Any, ViewBinding>>()

        /**
         * Adds the single [item] and the corresponding [delegate]
         */
        fun <T : Any, V: ViewBinding, H : AnyTypeViewHolder<T, V>> add(
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
        fun <T : Any, V: ViewBinding, H : AnyTypeViewHolder<T, V>> add(
            items: List<T>,
            delegate: AnyTypeDelegate<T, V, H>
        ): Builder {
            return apply { items.forEach { add(it, delegate) } }
        }

        /**
         * Adds section without data to bind
         */
        fun <V: ViewBinding> add(delegate: NoDataDelegate<V>): Builder {
            return apply { add(Unit, delegate) }
        }

        /**
         * Adds [item] and the corresponding [delegate] only if [predicate] is true
         * @param predicate the condition determining whether the items and delegate should be added
         */
        fun <T: Any, V: ViewBinding, H : AnyTypeViewHolder<T, V>> addIf(
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
        fun <T : Any, V: ViewBinding, H : AnyTypeViewHolder<List<T>, V>> addIf(
            items: List<T>,
            delegate: AnyTypeDelegate<List<T>, V, H>,
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
        fun <V: ViewBinding> addIf(delegate: NoDataDelegate<V>, predicate: () -> Boolean): Builder {
            return apply {
                if (predicate.invoke()) {
                    add(Unit, delegate)
                }
            }
        }

        /**
         * Adds [items] and the corresponding [delegate] only if the list of items is not empty
         */
        fun <T : Any, V : ViewBinding, H : AnyTypeViewHolder<List<T>, V>> addIfNotEmpty(
            items: List<T>,
            delegate: AnyTypeDelegate<List<T>, V, H>
        ): Builder {
            return apply { addIf(items, delegate) { items.isNotEmpty() } }
        }

        fun build(): AnyTypeCollection {
            val positionsRanges = with(itemsMetaData) {
                zipWithNext { first, second ->
                    first.position until second.position
                } + when {
                    size % 2 == 0 -> emptyList()
                    else -> listOf(last().position until items.size)
                }
            }
            return AnyTypeCollection(items, itemsMetaData, positionsRanges)
        }
    }

    companion object {
        val EMPTY = Builder().build()
    }
}