package vsukharev.anytypeadapter.sample.common.presentation.delegate

import vsukharev.anytypeadapter.item.AdapterItem

/**
 * [AdapterItem] representing header text
 */
class HeaderAdapterItem(val text: String) : AdapterItem {
    override fun areItemsTheSame(other: AdapterItem): Boolean = true

    override fun areContentsTheSame(other: AdapterItem): Boolean = true
}