package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity

import android.view.View
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.databinding.DelegateActivityBinding
import vsukharev.anytypeadapter.sample.feed.domain.model.Activity
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity.ActivityDelegate.Holder

class ActivityDelegate(
    private val onItemClickListener: (Activity) -> Unit
) : AnyTypeDelegate<Activity, DelegateActivityBinding, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(
        DelegateActivityBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_activity

    override fun getItemId(item: Activity): String = item.id

    inner class Holder(binding: DelegateActivityBinding) :
        AnyTypeViewHolder<Activity, DelegateActivityBinding>(binding) {
        private val iv = binding.delegateActivityIv
        private val tv = binding.delegateActivityTv

        override fun bind(item: Activity) {
            with(item) {
                iv.setOnClickListener { onItemClickListener.invoke(this) }
                iv.setImageResource(iconRes)
                tv.text = name
            }
        }
    }
}