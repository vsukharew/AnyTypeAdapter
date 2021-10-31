package vsukharev.anytypeadapter.delegate

import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.holder.NoDataViewHolder
import java.util.*

/**
 * [AnyTypeDelegate] that creates [NoDataViewHolder]
 * This delegate can be used when creating list for [AnyTypeAdapter] without specifying data to bind
 */
abstract class NoDataDelegate<V: ViewBinding> : AnyTypeDelegate<Unit, V, NoDataViewHolder<V>>() {
    /**
     * This delegate has an artificial identifier because there is no data
     */
    override fun getItemId(item: Unit): String = UUID.randomUUID().toString()
}