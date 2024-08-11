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
    val data: T,
    val itemViewType: Int,
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