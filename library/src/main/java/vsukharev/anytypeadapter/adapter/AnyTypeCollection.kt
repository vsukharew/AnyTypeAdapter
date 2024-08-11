package vsukharev.anytypeadapter.adapter

import androidx.viewbinding.ViewBinding
import androidx.recyclerview.widget.RecyclerView
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.delegate.NoDataDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem

/**
 * Class that wraps items for [AnyTypeAdapter]
 * @property items items to display in [AnyTypeAdapter]
 * @property itemViewTypesToDelegates association between itemViewType
 * and [AnyTypeDelegate] that creates [RecyclerView.ViewHolder] given itemViewType
 */
class AnyTypeCollection private constructor(
    val items: List<AdapterItem<Any>>,
    val itemViewTypesToDelegates: Map<Int, AnyTypeDelegate<Any, ViewBinding, AnyTypeViewHolder<Any, ViewBinding>>>
) {
    val size: Int = items.size

    class Builder {
        private val items = mutableListOf<AdapterItem<Any>>()
        private val itemViewTypesToDelegates =
            mutableMapOf<Int, AnyTypeDelegate<Any, ViewBinding, AnyTypeViewHolder<Any, ViewBinding>>>()

        /**
         * Adds the single [item] and the corresponding [delegate]
         */
        fun <T : Any, V : ViewBinding, H : AnyTypeViewHolder<T, V>> add(
            item: T,
            delegate: AnyTypeDelegate<T, V, H>
        ): Builder {
            return apply {
                val itemViewType = delegate.getItemViewType()
                val isCurrentViewTypeEqualToLastAdded = with(items) {
                    isNotEmpty() && get(lastIndex).itemViewType == itemViewType
                }

                // Put new view type only if it isn't equal to the previous one
                when {
                    !isCurrentViewTypeEqualToLastAdded -> {
                        /**
                         * Cast is safe because you don't need to know the exact type later
                         * and [AnyTypeCollection.itemViewTypesToDelegates] as well as [AnyTypeCollection.items] are immutable
                         * so you won't be able to write the wrong value without recreating [AnyTypeCollection]
                         */
                        @Suppress("UNCHECKED_CAST")
                        itemViewTypesToDelegates[itemViewType] =
                            delegate as AnyTypeDelegate<Any, ViewBinding, AnyTypeViewHolder<Any, ViewBinding>>
                    }

                    else -> {
                        /* do nothing */
                    }
                }
                val itemId = delegate.getItemId(item)
                items.add(AdapterItem(itemId, item, itemViewType))
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
            return apply { addIf(item, delegate) { item.any() } }
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
            return AnyTypeCollection(items, itemViewTypesToDelegates)
        }
    }

    companion object {
        val EMPTY = Builder().build()
    }
}