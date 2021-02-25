package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.delegate.PartiallyColoredHeaderDelegate.Holder
import vsukharev.anytypeadapter.sample.databinding.HeaderViewBinding

private const val SPACE = " "

/**
 * Delegate showing a header text which last word is colored
 * @param defaultColor the color applied to the text
 * @param highlightColor the color applied to the last word of the text
 */
class PartiallyColoredHeaderDelegate(
    @ColorRes private val defaultColor: Int,
    @ColorRes private val highlightColor: Int
) : AnyTypeDelegate<String, HeaderViewBinding, Holder>() {

    override fun createViewHolder(itemView: View) = Holder(HeaderViewBinding.bind(itemView))

    override fun getItemViewType(): Int = R.layout.header_view

    override fun getItemId(item: String): String = item

    inner class Holder(
        binding: HeaderViewBinding
    ) : AnyTypeViewHolder<String, HeaderViewBinding>(binding) {
        private val headerView: TextView = binding.headerDelegateHeaderTv

        override fun bind(item: String) {
            with(item) {
                with(itemView) {
                    val spannableString = SpannableString(item)
                    spannableString.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(context, highlightColor)
                        ),
                        lastIndexOf(SPACE).takeIf { it != -1 } ?: 0,
                        length,
                        SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                    headerView.setTextColor(ContextCompat.getColor(context, defaultColor))
                    headerView.text = spannableString
                }
            }
        }
    }
}