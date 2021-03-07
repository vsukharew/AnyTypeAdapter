package vsukharev.anytypeadapter.item

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder

/**
 * Class representing [AnyTypeAdapter] list item.
 * @param T type of data to bind in view holder
 * @param id unique identifier
 * @param data data to bind to view holder
 *
 * The methods replicate [DiffUtil.ItemCallback] ones
 */
data class AdapterItem<T>(
    val id: String,
    val data: T
) {
    /**
     * Checks if the items have equal identifiers
     * should be called inside [DiffUtil.ItemCallback.areItemsTheSame]
     */
    fun areItemsTheSame(other: AdapterItem<T>): Boolean = id == other.id

    /**
     * Checks if the items have all the fields values equal
     * should be called inside [DiffUtil.ItemCallback.areContentsTheSame]
     */
    fun areContentsTheSame(other: AdapterItem<T>): Boolean = data == other.data
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
 * @property delegate delegate creating appropriate [AnyTypeViewHolder] and binding data to
 */
data class AdapterItemMetaData<T, V: ViewBinding>(
    val position: Int,
    val delegate: AnyTypeDelegate<T, V, AnyTypeViewHolder<T, V>>
)