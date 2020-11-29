package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice

import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice.EditorsChoiceSectionDelegate.Holder
import java.util.UUID

/**
 * Delegate responsible for the whole "editor's choice" section
 */
class EditorsChoiceSectionDelegate : BaseDelegate<List<EditorsChoice>, Holder>() {
    private val delegate = EditorsChoiceDelegate()
    private val adapter = object : AnyTypeAdapter() {
        var collectionSize = 0

        override fun getItemCount(): Int {
            return when {
                super.getItemCount() == 0 -> 0
                else -> Int.MAX_VALUE
            }
        }

        override fun onBindViewHolder(holder: BaseViewHolder<Any>, position: Int) {
            val realPosition = position % collectionSize
            super.onBindViewHolder(holder, realPosition)
        }
    }

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_editors_choice_section

    override fun getItemId(item: List<EditorsChoice>): String = ITEM_ID

    inner class Holder(itemView: View) : BaseViewHolder<List<EditorsChoice>>(itemView) {
        private val recyclerView =
            itemView.findViewById<RecyclerView>(R.id.editors_choice_section_rv)

        init {
            recyclerView.adapter = adapter
            (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            PagerSnapHelper().attachToRecyclerView(recyclerView)
        }

        override fun bind(item: List<EditorsChoice>) {
            adapter.collectionSize = item.size
            Collection.Builder()
                .add(item, delegate)
                .build()
                .let { adapter.setCollection(it) { recyclerView.scrollToPosition(adapter.itemCount / 2) } }
        }
    }

    private companion object {
        val ITEM_ID: String = UUID.randomUUID().toString()
    }
}