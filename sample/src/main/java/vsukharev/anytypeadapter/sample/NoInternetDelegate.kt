package vsukharev.anytypeadapter.sample

import android.view.View
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import java.util.*

/**
 * Delegate showing no internet state
 */
class NoInternetDelegate : BaseDelegate<Unit, NoInternetDelegate.Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_no_internet

    override fun getItemId(item: Unit): String = ITEM_ID

    class Holder(itemView: View) : BaseViewHolder<Unit>(itemView) {
        override fun bind(item: Unit) {

        }
    }

    private companion object {
        private val ITEM_ID: String = UUID.randomUUID().toString()
    }
}