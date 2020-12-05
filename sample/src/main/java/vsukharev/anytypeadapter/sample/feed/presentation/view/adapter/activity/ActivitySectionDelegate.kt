package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity

import android.view.View
import kotlinx.android.synthetic.main.delegate_activity_section.view.*
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.domain.model.Activity
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity.ActivitySectionDelegate.Holder

class ActivitySectionDelegate : BaseDelegate<List<Activity>, Holder>() {
    private val delegate = ActivityDelegate()
    private val anyTypeAdapter = AnyTypeAdapter()

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_activity_section

    override fun getItemId(item: List<Activity>): String = ITEM_ID

    inner class Holder(itemView: View) : BaseViewHolder<List<Activity>>(itemView) {
        init {
            itemView.delegate_activity_section_rv.adapter = anyTypeAdapter
        }

        override fun bind(item: List<Activity>) {
            Collection.Builder()
                .add(item, delegate)
                .build()
                .let { anyTypeAdapter.setCollection(it) }
        }
    }

    private companion object {
        val ITEM_ID: String = java.util.UUID.randomUUID().toString()
    }
}