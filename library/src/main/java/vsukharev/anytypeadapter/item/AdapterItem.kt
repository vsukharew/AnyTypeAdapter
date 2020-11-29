package vsukharev.anytypeadapter.item

import androidx.recyclerview.widget.DiffUtil
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter

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
     * Checks if the items have equal all fields values
     * should be called inside [DiffUtil.ItemCallback.areContentsTheSame]
     */
    fun <T> areContentsTheSame(other: AdapterItem<T>): Boolean where T: Any = data == other.data
}