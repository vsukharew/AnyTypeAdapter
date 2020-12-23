package vsukharev.anytypeadapter.delegate

import android.view.View
import vsukharev.anytypeadapter.holder.NoDataViewHolder
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import java.util.*

/**
 * [BaseDelegate] that creates [NoDataViewHolder]
 * This delegate can be used when creating list for [AnyTypeAdapter] without specifying data to bind
 */
abstract class NoDataDelegate : BaseDelegate<Unit, NoDataViewHolder>() {

    override fun getItemId(item: Unit): String = ITEM_ID

    override fun createViewHolder(itemView: View): NoDataViewHolder = NoDataViewHolder(itemView)

    private companion object {
        /**
         * This delegate has an "artificial" identifier because there is no data
         */
        val ITEM_ID: String = UUID.randomUUID().toString()
    }
}