package vsukharev.anytypeadapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter

/**
 * The base class for [AnyTypeAdapter] view holders
 */
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * Sets the item fields values to views
     */
    abstract fun bind(item: T)
}