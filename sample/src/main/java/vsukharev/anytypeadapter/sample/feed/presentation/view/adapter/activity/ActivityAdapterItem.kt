package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity

import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.sample.feed.domain.model.Activity

data class ActivityAdapterItem(val activity: Activity) : AdapterItem {

    override fun areItemsTheSame(other: AdapterItem): Boolean =
        other is ActivityAdapterItem && other.activity.id == activity.id

    override fun areContentsTheSame(other: AdapterItem): Boolean =
        other is ActivityAdapterItem && other.activity == activity
}