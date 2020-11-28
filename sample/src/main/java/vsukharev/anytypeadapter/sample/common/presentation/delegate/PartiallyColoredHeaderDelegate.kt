package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

private const val SPACE = " "

/**
 * Delegate showing a header text which last word is colored
 * @param defaultColor the color applied to the text
 * @param highlightColor the color applied to the last word of the text
 */
class PartiallyColoredHeaderDelegate(
    @ColorRes defaultColor: Int,
    @ColorRes private val highlightColor: Int
) : HeaderDelegate(defaultColor) {
    override fun createViewHolder(itemView: View): HeaderViewHolder =
        PartiallyColoredHeaderViewHolder(itemView)

    inner class PartiallyColoredHeaderViewHolder(itemView: View) : HeaderViewHolder(itemView) {
        override fun bind(item: String) {
            with(item) {
                with(itemView) {
                    val spannableString = SpannableString(item)
                    spannableString.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(context, highlightColor)
                        ),
                        lastIndexOf(SPACE),
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