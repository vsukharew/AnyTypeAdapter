package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity

import android.view.View
import kotlinx.android.synthetic.main.delegate_activity_section.view.*
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity.ActivitySectionDelegate.Holder

class ActivitySectionDelegate : BaseDelegate<ActivitySectionAdapterItem, Holder>() {
    private val delegate = ActivityDelegate()

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_activity_section

    inner class Holder(itemView: View) : BaseViewHolder<ActivitySectionAdapterItem>(itemView) {
        private val recyclerView = itemView.delegate_activity_section_rv
        private val adapter = AnyTypeAdapter()

        init {
            recyclerView.adapter = adapter
        }

        override fun bind(item: ActivitySectionAdapterItem) {
            Collection.Builder()
                .add(item.activityItems, delegate)
                .build()
                .let { adapter.setItems(it) }
        }
    }
}