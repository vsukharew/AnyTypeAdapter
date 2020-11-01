package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.delegate.IconWithTextDelegate.Holder

/**
 * The delegate responsible for the image with text section creation
 */
class IconWithTextDelegate : BaseDelegate<IconWithTextAdapterItem, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_icon_with_text

    class Holder(itemView: View) : BaseViewHolder<IconWithTextAdapterItem>(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.delegate_icon_with_text_iv)
        private val textView: TextView = itemView.findViewById(R.id.delegate_icon_with_text_tv)

        override fun bind(item: IconWithTextAdapterItem) {
            with(item) {
                imageView.setImageResource(imageResId)
                textView.setText(textResId)
            }
        }
    }
}