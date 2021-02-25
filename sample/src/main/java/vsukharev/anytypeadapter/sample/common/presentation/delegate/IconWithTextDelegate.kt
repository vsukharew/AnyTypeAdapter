package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.delegate.IconWithTextDelegate.Holder
import vsukharev.anytypeadapter.sample.databinding.DelegateIconWithTextBinding

/**
 * The delegate responsible for the image with text section creation
 */
class IconWithTextDelegate(
    private val onItemClickListener: (IconWithTextAdapterItem) -> Unit
) : AnyTypeDelegate<IconWithTextAdapterItem, DelegateIconWithTextBinding, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(
        DelegateIconWithTextBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_icon_with_text

    override fun getItemId(item: IconWithTextAdapterItem): String = item.id

    inner class Holder(
        binding: DelegateIconWithTextBinding
    ) : AnyTypeViewHolder<IconWithTextAdapterItem, DelegateIconWithTextBinding>(binding) {
        private val rootLayout: LinearLayout = binding.delegateIconWithTextRootLayout
        private val imageView: ImageView = binding.delegateIconWithTextIv
        private val textView: TextView = binding.delegateIconWithTextTv

        override fun bind(item: IconWithTextAdapterItem) {
            with(item) {
                rootLayout.setOnClickListener { onItemClickListener.invoke(this) }
                imageView.setImageResource(imageResId)
                textView.text = text
            }
        }
    }
}