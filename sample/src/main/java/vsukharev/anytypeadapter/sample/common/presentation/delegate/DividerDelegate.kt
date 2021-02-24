package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import androidx.core.view.updateLayoutParams
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.delegate.DividerDelegate.Holder
import vsukharev.anytypeadapter.sample.databinding.DelegateDividerBinding

/**
 * The delegate responsible for the divider section creation
 */
class DividerDelegate : AnyTypeDelegate<Int, DelegateDividerBinding, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(
        DelegateDividerBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_divider

    override fun getItemId(item: Int): String = item.toString()

    class Holder(binding: DelegateDividerBinding) : AnyTypeViewHolder<Int, DelegateDividerBinding>(binding) {

        private val view: View = binding.delegateDividerView

        override fun bind(item: Int) {
            view.updateLayoutParams {
                height = context.resources.getDimension(item).toInt()
            }
        }
    }
}