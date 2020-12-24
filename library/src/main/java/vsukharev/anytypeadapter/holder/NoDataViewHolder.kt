package vsukharev.anytypeadapter.holder

import android.view.View

/**
 * [AnyTypeViewHolder] that has no data to bind
 */
open class NoDataViewHolder(itemView: View) : AnyTypeViewHolder<Unit>(itemView) {
    override fun bind(item: Unit) {
        // empty implementation
    }
}