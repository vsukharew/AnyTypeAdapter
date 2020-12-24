package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import androidx.core.view.updateLayoutParams
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.delegate.DividerDelegate.Holder

/**
 * The delegate responsible for the divider section creation
 */
class DividerDelegate : BaseDelegate<Int, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_divider

    override fun getItemId(item: Int): String = item.toString()

    class Holder(itemView: View) : BaseViewHolder<Int>(itemView) {

        private val view: View = itemView.findViewById(R.id.delegate_divider_view)

        override fun bind(item: Int) {
            view.updateLayoutParams {
                height = context.resources.getDimension(item).toInt()
            }
        }
    }
}