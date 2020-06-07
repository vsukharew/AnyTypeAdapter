package vsukharev.anytypeadapter.delegate

import android.view.View
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * The base delegate class for [AnyTypeAdapter] item
 *
 * A delegate binds the data to view holder.
 *
 * It also describes
 * - what type of [RecyclerView.ViewHolder] will be created
 * - of what view type this [AnyTypeAdapter] item is
 *
 * @see [RecyclerView.Adapter.onCreateViewHolder]
 * @see [RecyclerView.Adapter.getItemViewType]
 */
abstract class BaseDelegate<T : AdapterItem, H: BaseViewHolder<T>> {
    /**
     * Creates a view holder.
     * Should be called inside [RecyclerView.Adapter.onCreateViewHolder]
     */
    abstract fun createViewHolder(itemView: View): H

    /**
     * Returns a view type for this [AnyTypeAdapter] item.
     * Should be called inside [RecyclerView.Adapter.onCreateViewHolder]
     */
    abstract fun getItemViewType(): Int

    /**
     * Binds the data to view holder
     * Should be called inside [RecyclerView.Adapter.onBindViewHolder]
     */
    fun bind(data: T, holder: H) {
        holder.bind(data)
    }
}