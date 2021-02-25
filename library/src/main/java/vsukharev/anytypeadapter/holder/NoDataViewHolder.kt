package vsukharev.anytypeadapter.holder

import androidx.viewbinding.ViewBinding

/**
 * [AnyTypeViewHolder] that has no data to bind
 */
open class NoDataViewHolder<V: ViewBinding>(viewBinding: V)
    : AnyTypeViewHolder<Unit, V>(viewBinding) {
    override fun bind(item: Unit) {
        // empty implementation
    }
}