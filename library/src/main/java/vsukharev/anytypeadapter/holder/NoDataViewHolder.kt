package vsukharev.anytypeadapter.holder

import android.view.View

/**
 * [BaseViewHolder] that has no data to bind
 */
open class NoDataViewHolder(itemView: View) : BaseViewHolder<Unit>(itemView) {
    override fun bind(item: Unit) {
        // empty implementation
    }
}