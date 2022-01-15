package vsukharev.anytypeadapter.holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate

/**
 * The base class for [AnyTypeAdapter] view holders
 */
abstract class AnyTypeViewHolder<T, V : ViewBinding>(
    viewBinding: V
) : RecyclerView.ViewHolder(viewBinding.root) {
    protected val context: Context = itemView.context

    /**
     * Sets the item fields values to views
     */
    abstract fun bind(item: T)

    /**
     * Sets to views only that values that were changed and returned from [AnyTypeDelegate.getChangePayload].
     *
     * Serves as partial item binding
     */
    open fun applyPayload(payloads: List<Any>) {}
}