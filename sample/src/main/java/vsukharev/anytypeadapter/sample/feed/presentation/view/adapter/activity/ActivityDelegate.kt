package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity

import android.view.View
import kotlinx.android.synthetic.main.delegate_activity.view.*
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.domain.model.Activity
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity.ActivityDelegate.Holder

class ActivityDelegate(
    private val onItemClickListener: (Activity) -> Unit
) : AnyTypeDelegate<Activity, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_activity

    override fun getItemId(item: Activity): String = item.id

    inner class Holder(itemView: View) : AnyTypeViewHolder<Activity>(itemView) {
        private val iv = itemView.delegate_activity_iv
        private val tv = itemView.delegate_activity_tv

        override fun bind(item: Activity) {
            with(item) {
                iv.setOnClickListener { onItemClickListener.invoke(this) }
                iv.setImageResource(iconRes)
                tv.text = name
            }
        }
    }
}