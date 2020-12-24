package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import kotlinx.android.synthetic.main.delegate_header_with_button.view.*
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R

/**
 * The delegate responsible for a text with button creation
 */
class HeaderWithButtonDelegate :
    BaseDelegate<HeaderWithButtonAdapterItem, BaseViewHolder<HeaderWithButtonAdapterItem>>() {

    override fun createViewHolder(itemView: View): ButtonViewHolder = ButtonViewHolder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_header_with_button

    override fun getItemId(item: HeaderWithButtonAdapterItem): String = item.id

    class ButtonViewHolder(itemView: View) : BaseViewHolder<HeaderWithButtonAdapterItem>(itemView) {
        private val textView: TextView = itemView.delegate_header_with_button_tv
        private val button: AppCompatButton = itemView.delegate_header_with_button_btn

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