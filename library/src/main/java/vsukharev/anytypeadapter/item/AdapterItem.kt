package vsukharev.anytypeadapter.item

import androidx.recyclerview.widget.DiffUtil
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder

/**
 * Class representing [AnyTypeAdapter] list item.
 * @param T type of data to bind in view holder
 * @param id unique identifier
 * @param data data to bind to view holder
 *
 * The methods replicate [DiffUtil.ItemCallback] ones
 */
data class AdapterItem<T: Any>(
    val id: String,
    val data: T
) {
    /**
     * Checks if the items have equal identifiers
     * should be called inside [DiffUtil.ItemCallback.areItemsTheSame]
     */
    fun <T> areItemsTheSame(other: AdapterItem<T>): Boolean where T: Any = id == other.id

    /**
     * Checks if the items have all the fields values equal
     * should be called inside [DiffUtil.ItemCallback.areContentsTheSame]
     */
    fun <T> areContentsTheSame(other: AdapterItem<T>): Boolean where T: Any = data == other.data
}

/**
 * Metadata for [AdapterItem]
 *
 * This class helps to determine which data at which position should be bound
 *
 * This class is intentionally created separately from [AdapterItem]
 * because one instance of this class provides metadata about each [AdapterItem] instance
 * of the given type
 *
 * @property position position the first [AdapterItem] of the given type is placed at
 * @property delegate delegate creating appropriate [BaseViewHolder] and binding data to
 */
data class AdapterItemMetaData<T: Any>(
    val position: Int,
    val delegate: BaseDelegate<T, BaseViewHolder<T>>
)