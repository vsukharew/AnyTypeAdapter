package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.delegate_icon_with_text.view.*
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.delegate.IconWithTextDelegate.Holder

/**
 * The delegate responsible for the image with text section creation
 */
class IconWithTextDelegate(
    private val onItemClickListener: (IconWithTextAdapterItem) -> Unit
) : BaseDelegate<IconWithTextAdapterItem, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_icon_with_text

    override fun getItemId(item: IconWithTextAdapterItem): String = item.id

    inner class Holder(itemView: View) : BaseViewHolder<IconWithTextAdapterItem>(itemView) {
        private val rootLayout: LinearLayout = itemView.delegate_icon_with_text_root_layout
        private val imageView: ImageView = itemView.delegate_icon_with_text_iv
        private val textView: TextView = itemView.delegate_icon_with_text_tv

        override fun bind(item: IconWithTextAdapterItem) {
            with(item) {
                rootLayout.setOnClickListener { onItemClickListener.invoke(this) }
                imageView.setImageResource(imageResId)
                textView.text = text
            }
        }
    }
}