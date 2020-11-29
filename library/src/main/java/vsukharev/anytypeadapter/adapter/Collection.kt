package vsukharev.anytypeadapter.adapter

import android.util.SparseArray
import android.util.SparseIntArray
import androidx.core.util.set
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.item.AdapterItem

/**
 * The class that wraps items for [AnyTypeAdapter] adding the following mappings
 * - positions to view types
 * - view types to [BaseDelegate]s
 */
class Collection private constructor(
    val items: List<AdapterItem<Any>>,
    val viewTypeToDelegateMap: SparseArray<BaseDelegate<Any, BaseViewHolder<Any>>>,
    val positionToViewTypeMap: SparseIntArray
) {
    val size: Int = items.size

    class Builder {
        private val list = mutableListOf<AdapterItem<Any>>()
        private val viewTypeToDelegateMap =
            SparseArray<BaseDelegate<Any, BaseViewHolder<Any>>>()
        private val positionToViewTypeMap = SparseIntArray()

        /**
         * Adds the single item and the corresponding controller
         */
        fun <T: Any, H : BaseViewHolder<T>> add(
            item: T,
            controller: BaseDelegate<T, H>
        ): Builder {
            return apply {
                // put corresponding viewType by key equal the size of the items collection
                positionToViewTypeMap[list.size] = controller.getItemViewType()
                list.add(AdapterItem(controller.getItemId(item), item))
                // put corresponding controller by key equal the viewType of the current item
                viewTypeToDelegateMap[controller.getItemViewType()] =
                    controller as BaseDelegate<Any, BaseViewHolder<Any>>
                // as a result, have two collections:
                // the first that represents the positions range items at which have the same viewType
                // and the second that shows which controller each viewType has
            }
        }

        /**
         * Adds items and the corresponding controller
         */
        fun <T: Any, H : BaseViewHolder<T>> add(
            items: List<T>,
            controller: BaseDelegate<T, H>
        ): Builder {
            return apply {
                items.forEach {
                    positionToViewTypeMap[list.size] = controller.getItemViewType()
                    list.add(AdapterItem(controller.getItemId(it), it))
                }
                viewTypeToDelegateMap[controller.getItemViewType()] =
                    controller as BaseDelegate<Any, BaseViewHolder<Any>>
            }
        }

        /**
         * Adds items and the corresponding controller only if predicate is true
         * @param predicate the condition determining whether the items and controller should be added
         */
        fun <T: Any, H : BaseViewHolder<List<T>>> addIf(
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
        fun <T: Any, H : BaseViewHolder<List<T>>> addIfNotEmpty(
            items: List<T>,
            controller: BaseDelegate<List<T>, H>
        ): Builder {
            return apply { addIf(items, controller) { items.isNotEmpty() } }
        }

        fun build() = Collection(list, viewTypeToDelegateMap, positionToViewTypeMap)
    }

    companion object {
        val EMPTY = Builder().build()
    }
}