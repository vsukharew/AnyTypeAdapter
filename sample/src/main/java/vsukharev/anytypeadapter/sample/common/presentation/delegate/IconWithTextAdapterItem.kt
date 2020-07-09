package vsukharev.anytypeadapter.sample.common.presentation.delegate

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import vsukharev.anytypeadapter.item.AdapterItem

/**
 * [AdapterItem] describing a row with an image and text
 */
class IconWithTextAdapterItem(
    @DrawableRes val imageResId: Int,
    @StringRes val textResId: Int
) : AdapterItem {
    override fun areItemsTheSame(other: AdapterItem): Boolean = true
    override fun areContentsTheSame(other: AdapterItem): Boolean = true
}