package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.delegate.HeaderWithButtonDelegate.ButtonViewHolder
import vsukharev.anytypeadapter.sample.databinding.DelegateHeaderWithButtonBinding

/**
 * The delegate responsible for a text with button creation
 */
class HeaderWithButtonDelegate :
    AnyTypeDelegate<
            HeaderWithButtonAdapterItem,
            DelegateHeaderWithButtonBinding,
            ButtonViewHolder>() {

    override fun createViewHolder(itemView: View) = ButtonViewHolder(
        DelegateHeaderWithButtonBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_header_with_button

    override fun getItemId(item: HeaderWithButtonAdapterItem): String = item.id

    class ButtonViewHolder(binding: DelegateHeaderWithButtonBinding) :
        AnyTypeViewHolder<HeaderWithButtonAdapterItem, DelegateHeaderWithButtonBinding>(binding) {
        private val textView: TextView = binding.delegateHeaderWithButtonTv
        private val button: AppCompatButton = binding.delegateHeaderWithButtonBtn

        override fun bind(item: HeaderWithButtonAdapterItem) {
            with(item) {
                val headerText = context.getText(headerTextResId)
                val btnText = context.getText(btnTextResId)
                textView.setText(headerTextResId)
                button.setText(btnTextResId)
                button.setOnClickListener { onClickListener?.invoke("$headerText - $btnText") }
            }
        }
    }
}