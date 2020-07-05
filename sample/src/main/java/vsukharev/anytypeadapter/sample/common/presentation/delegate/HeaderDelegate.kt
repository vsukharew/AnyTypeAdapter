package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R

/**
 * Delegate showing a header text
 * @param defaultColor the color applied to the text
 */
open class HeaderDelegate(
    @ColorRes protected val defaultColor: Int
) : BaseDelegate<HeaderAdapterItem, HeaderDelegate.HeaderViewHolder>() {

    override fun createViewHolder(itemView: View): HeaderViewHolder  = HeaderViewHolder(itemView)

    override fun getItemViewType(): Int = R.layout.header_view

    open inner class HeaderViewHolder(itemView: View) : BaseViewHolder<HeaderAdapterItem>(itemView) {
        protected val headerView: TextView = itemView.findViewById(R.id.header_delegate_header_tv)

        override fun bind(item: HeaderAdapterItem) {
            headerView.setTextColor(ContextCompat.getColor(itemView.context, defaultColor))
            headerView.text = item.text
        }
    }
}