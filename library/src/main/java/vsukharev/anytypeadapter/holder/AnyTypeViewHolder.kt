package vsukharev.anytypeadapter.holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter

/**
 * The base class for [AnyTypeAdapter] view holders
 */
abstract class AnyTypeViewHolder<T, V: ViewBinding>(
    viewBinding: V
) : RecyclerView.ViewHolder(viewBinding.root) {
    protected val context: Context = itemView.context

    /**
     * Sets the item fields values to views
     */
    abstract fun bind(item: T)
}