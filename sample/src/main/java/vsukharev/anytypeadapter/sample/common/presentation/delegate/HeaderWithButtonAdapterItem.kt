package vsukharev.anytypeadapter.sample.common.presentation.delegate

import androidx.annotation.StringRes
import vsukharev.anytypeadapter.item.AdapterItem

/**
 * The adapter item describing the view that consists of a text and a button
 */
class HeaderWithButtonAdapterItem(
    @StringRes val headerTextResId: Int,
    @StringRes val btnTextResId: Int,
    val onClickListener: (() -> Unit)? = null
) : AdapterItem {
    override fun areItemsTheSame(other: AdapterItem): Boolean = true

    override fun areContentsTheSame(other: AdapterItem): Boolean = true
}