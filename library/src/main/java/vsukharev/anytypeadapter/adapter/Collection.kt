package vsukharev.anytypeadapter.adapter

import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.item.AdapterItemMetaData
import androidx.recyclerview.widget.RecyclerView
import vsukharev.anytypeadapter.delegate.NoDataDelegate

/**
 * Class that wraps items for [AnyTypeAdapter]
 * @property items items to display in [AnyTypeAdapter]
 * @property itemsMetaData metadata for items to display. Helps to determine which data at which position should be bound
 */
class Collection private constructor(
    val items: List<AdapterItem<Any>>,
    val itemsMetaData: List<AdapterItemMetaData<Any>>
) {
    /**
     * Saved position value provided in [RecyclerView.Adapter.getItemViewType]
     */
    var currentItemViewTypePosition: Int = 0
    val size: Int = items.size

    /**
     * Returns delegate at the given position in the [itemsMetaData] collection
     */
    fun delegateAt(position: Int): AnyTypeDelegate<Any, AnyTypeViewHolder<Any>> =
        itemsMetaData[position].delegate

    class Builder {
        private val items = mutableListOf<AdapterItem<Any>>()
        private val itemsMetaData = mutableListOf<AdapterItemMetaData<Any>>()

        /**
         * Adds the single [item] and the corresponding [delegate]
         */
        fun <T : Any, H : AnyTypeViewHolder<T>> add(
            item: T,
            delegate: AnyTypeDelegate<T, H>
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
                         * and [Collection.itemsMetaData] as well as [Collection.items] are immutable
                         * so you won't be able to write the wrong value without recreating [Collection]
                         */
                        @Suppress("UNCHECKED_CAST")
                        itemsMetaData.add(
                            AdapterItemMetaData(
                                items.size,
                                delegate as AnyTypeDelegate<Any, AnyTypeViewHolder<Any>>
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
        fun <T : Any, H : AnyTypeViewHolder<T>> add(
            items: List<T>,
            delegate: AnyTypeDelegate<T, H>
        ): Builder {
            return apply { items.forEach { add(it, delegate) } }
        }

        /**
         * Adds section without data to bind
         */
        fun add(delegate: NoDataDelegate): Builder {
            return apply { add(Unit, delegate) }
        }

        /**
         * Adds [item] and the corresponding [delegate] only if [predicate] is true
         * @param predicate the condition determining whether the items and delegate should be added
         */
        fun <T: Any, H : AnyTypeViewHolder<T>> addIf(
            item: T,
            delegate: AnyTypeDelegate<T, H>,
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
        fun <T : Any, H : AnyTypeViewHolder<List<T>>> addIf(
            items: List<T>,
            delegate: AnyTypeDelegate<List<T>, H>,
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
        fun addIf(delegate: NoDataDelegate, predicate: () -> Boolean): Builder {
            return apply {
                if (predicate.invoke()) {
                    add(Unit, delegate)
                }
            }
        }

        /**
         * Adds [items] and the corresponding [delegate] only if the list of items is not empty
         */
        fun <T : Any, H : AnyTypeViewHolder<List<T>>> addIfNotEmpty(
            items: List<T>,
            delegate: AnyTypeDelegate<List<T>, H>
        ): Builder {
            return apply { addIf(items, delegate) { items.isNotEmpty() } }
        }

        fun build() = Collection(items, itemsMetaData)
    }

    companion object {
        val EMPTY = Builder().build()
    }
}