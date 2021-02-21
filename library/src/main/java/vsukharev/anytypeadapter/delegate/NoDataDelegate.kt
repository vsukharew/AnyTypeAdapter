package vsukharev.anytypeadapter.delegate

import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.holder.NoDataViewHolder
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import java.util.*

/**
 * [AnyTypeDelegate] that creates [NoDataViewHolder]
 * This delegate can be used when creating list for [AnyTypeAdapter] without specifying data to bind
 */
abstract class NoDataDelegate<V: ViewBinding> : AnyTypeDelegate<Unit, V, NoDataViewHolder<V>>() {

    override fun getItemId(item: Unit): String = ITEM_ID

    private companion object {
        /**
         * This delegate has an "artificial" identifier because there is no data
         */
        val ITEM_ID: String = UUID.randomUUID().toString()
    }
}