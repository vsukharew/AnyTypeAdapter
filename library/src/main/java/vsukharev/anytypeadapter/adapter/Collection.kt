package vsukharev.anytypeadapter.adapter

import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.item.AdapterItemMetaData
import androidx.recyclerview.widget.RecyclerView

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
    fun delegateAt(position: Int): BaseDelegate<Any, BaseViewHolder<Any>> =
        itemsMetaData[position].delegate

    class Builder {
        private val items = mutableListOf<AdapterItem<Any>>()
        private val itemsMetaData = mutableListOf<AdapterItemMetaData<Any>>()

        /**
         * Adds the single item and the corresponding controller
         */
        fun <T : Any, H : BaseViewHolder<T>> add(
            item: T,
            controller: BaseDelegate<T, H>
        ): Builder {
            return apply {
                val isCurrentViewTypeEqualToLastAdded = with(itemsMetaData) {
                    isNotEmpty() && get(lastIndex).delegate.getItemViewType() == controller.getItemViewType()
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
                                controller as BaseDelegate<Any, BaseViewHolder<Any>>
                            )
                        )
                    }
                    else -> {
                        /* do nothing */
                    }
                }
                items.add(AdapterItem(controller.getItemId(item), item))
            }
        }

        /**
         * Adds items and the corresponding controller
         */
        fun <T : Any, H : BaseViewHolder<T>> add(
            items: List<T>,
            controller: BaseDelegate<T, H>
        ): Builder {
            return apply { items.forEach { add(it, controller) } }
        }

        /**
         * Adds item and the corresponding controller only if predicate is true
         * @param predicate the condition determining whether the items and controller should be added
         */
        fun <T: Any, H : BaseViewHolder<T>> addIf(
            item: T,
            controller: BaseDelegate<T, H>,
            predicate: () -> Boolean
        ): Builder {
            return apply {
                if (predicate.invoke()) {
                    add(item, controller)
                }
            }
        }

        /**
         * Adds items and the corresponding controller only if predicate is true
         * @param predicate the condition determining whether the items and controller should be added
         */
        fun <T : Any, H : BaseViewHolder<List<T>>> addIf(
            items: List<T>,
            controller: BaseDelegate<List<T>, H>,
            predicate: () -> Boolean
        ): Builder {
            return apply {
                if (predicate.invoke()) {
                    add(items, controller)
                }
            }
        }

        /**
         * Adds items and the corresponding controller only if the list of items is not empty
         */
        fun <T : Any, H : BaseViewHolder<List<T>>> addIfNotEmpty(
            items: List<T>,
            controller: BaseDelegate<List<T>, H>
        ): Builder {
            return apply { addIf(items, controller) { items.isNotEmpty() } }
        }

        fun build() = Collection(items, itemsMetaData)
    }

    companion object {
        val EMPTY = Builder().build()
    }
}