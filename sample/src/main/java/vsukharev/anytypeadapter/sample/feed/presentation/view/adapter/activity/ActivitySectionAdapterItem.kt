package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity

import vsukharev.anytypeadapter.item.AdapterItem

data class ActivitySectionAdapterItem(val activityItems: List<ActivityAdapterItem>) : AdapterItem {

    override fun areItemsTheSame(other: AdapterItem): Boolean = true

    override fun areContentsTheSame(other: AdapterItem): Boolean =
        other is ActivitySectionAdapterItem && activityItems == other.activityItems
}