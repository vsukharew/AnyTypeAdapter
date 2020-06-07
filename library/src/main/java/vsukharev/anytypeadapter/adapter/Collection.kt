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
    val items: List<AdapterItem>,
    val viewTypeToDelegateMap: SparseArray<BaseDelegate<AdapterItem, BaseViewHolder<AdapterItem>>>,
    val positionToViewTypeMap: SparseIntArray
) {
    val size: Int = items.size

    class Builder {
        private val list = mutableListOf<AdapterItem>()
        private val viewTypeToControllerMap =
            SparseArray<BaseDelegate<AdapterItem, BaseViewHolder<AdapterItem>>>()
        private val positionToViewTypeMap = SparseIntArray()

        /**
         * Adds the single item and the corresponding controller
         */
        fun <T : AdapterItem, H : BaseViewHolder<T>> add(
            item: T,
            controller: BaseDelegate<T, H>
        ): Builder {
            return apply {
                // put corresponding viewType by key equal the size of the items collection
                positionToViewTypeMap[list.size] = controller.getItemViewType()
                list.add(item)
                // put corresponding controller by key equal the viewType of the current item
                viewTypeToControllerMap[controller.getItemViewType()] =
                    controller as BaseDelegate<AdapterItem, BaseViewHolder<AdapterItem>>

                // as a result, have two collections:
                // the first that represents the positions range items at which have the same viewType
                // and the second that shows which controller each viewType has
            }
        }

        /**
         * Adds items and the corresponding controller
         */
        fun <T : AdapterItem, H : BaseViewHolder<T>> add(
            items: List<T>,
            controller: BaseDelegate<T, H>
        ): Builder {
            return apply {
                for (item in items) {
                    add(item, controller)
                }
            }
        }

        /**
         * Adds items and the corresponding controller only if predicate is true
         * @param predicate the condition determining whether the items and controller should be added
         */
        fun <T : AdapterItem, H : BaseViewHolder<T>> addIf(
            items: List<T>,
            controller: BaseDelegate<T, H>,
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
        fun <T : AdapterItem, H : BaseViewHolder<T>> addIfNotEmpty(
            items: List<T>,
            controller: BaseDelegate<T, H>
        ): Builder {
            return apply { addIf(items, controller) { items.isNotEmpty() } }
        }

        fun build() = Collection(
            list,
            viewTypeToControllerMap,
            positionToViewTypeMap
        )
    }

    companion object {
        val EMPTY = Builder().build()
    }
}