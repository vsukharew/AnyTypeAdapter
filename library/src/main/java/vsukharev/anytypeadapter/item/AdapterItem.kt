package vsukharev.anytypeadapter.item

import androidx.recyclerview.widget.DiffUtil
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter

/**
 * Interface for [AnyTypeAdapter] item.
 * The methods replicate [DiffUtil.ItemCallback] ones
 */
interface AdapterItem {
    /**
     * Checks if the items have equal identifiers
     * should be called inside [DiffUtil.ItemCallback.areItemsTheSame]
     */
    fun areItemsTheSame(other: AdapterItem): Boolean

    /**
     * Checks if the items have equal all fields values
     * should be called inside [DiffUtil.ItemCallback.areContentsTheSame]
     */
    fun areContentsTheSame(other: AdapterItem): Boolean
}