package vsukharev.anytypeadapter.sample.common.presentation.delegate

import androidx.annotation.DimenRes
import vsukharev.anytypeadapter.item.AdapterItem

/**
 * [AdapterItem] describing a divider section
 * @param height the divider height
 */
class DividerAdapterItem(@DimenRes val height: Int) : AdapterItem {
    override fun areItemsTheSame(other: AdapterItem): Boolean = true
    override fun areContentsTheSame(other: AdapterItem): Boolean = true
}