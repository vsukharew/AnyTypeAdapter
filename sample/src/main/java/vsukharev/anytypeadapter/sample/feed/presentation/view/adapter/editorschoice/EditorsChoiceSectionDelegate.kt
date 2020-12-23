package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
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
    private val anyTypeAdapter = object : AnyTypeAdapter() {
        override fun getItemCount(): Int {
            return when {
                super.getItemCount() == 0 -> 0
                else -> Int.MAX_VALUE
            }
        }

        override fun onBindViewHolder(holder: BaseViewHolder<Any>, position: Int) {
            val realPosition = position % collection.size
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
            recyclerView.apply {
                adapter = anyTypeAdapter
                layoutManager = object : LinearLayoutManager(
                    itemView.context,
                    RecyclerView.HORIZONTAL,
                    false
                ) {
                    override fun supportsPredictiveItemAnimations(): Boolean = false
                }
                PagerSnapHelper().attachToRecyclerView(this)
            }
        }

        override fun bind(item: List<EditorsChoice>) {
            Collection.Builder()
                .add(item, delegate)
                .build()
                .let {
                    anyTypeAdapter.setCollection(it) {
                        recyclerView.scrollToPosition(anyTypeAdapter.itemCount / 2)
                    }
                }
        }
    }

    private companion object {
        val ITEM_ID: String = UUID.randomUUID().toString()
    }
}