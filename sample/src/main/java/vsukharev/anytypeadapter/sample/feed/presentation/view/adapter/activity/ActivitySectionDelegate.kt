package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity

import android.view.View
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.AnyTypeCollection
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.databinding.DelegateActivitySectionBinding
import vsukharev.anytypeadapter.sample.feed.domain.model.Activity
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity.ActivitySectionDelegate.Holder

class ActivitySectionDelegate(
    onItemClickListener: (Activity) -> Unit
) : AnyTypeDelegate<List<Activity>, DelegateActivitySectionBinding, Holder>() {
    private val delegate = ActivityDelegate(onItemClickListener)
    private val anyTypeAdapter = AnyTypeAdapter()

    override fun createViewHolder(itemView: View) = Holder(
        DelegateActivitySectionBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_activity_section

    override fun getItemId(item: List<Activity>): String = ITEM_ID

    inner class Holder(
        binding: DelegateActivitySectionBinding
    ) : AnyTypeViewHolder<List<Activity>, DelegateActivitySectionBinding>(binding) {
        init {
            binding.delegateActivitySectionRv.adapter = anyTypeAdapter
        }

        override fun bind(item: List<Activity>) {
            AnyTypeCollection.Builder()
                .add(item, delegate)
                .build()
                .let { anyTypeAdapter.setCollection(it) }
        }
    }

    private companion object {
        val ITEM_ID: String = java.util.UUID.randomUUID().toString()
    }
}