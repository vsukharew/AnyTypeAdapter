package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.AnyTypeCollection
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.databinding.DelegateEditorsChoiceSectionBinding
import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice.EditorsChoiceSectionDelegate.Holder
import java.util.*

/**
 * Delegate responsible for the whole "editor's choice" section
 */
class EditorsChoiceSectionDelegate(
    onItemClickListener: (EditorsChoice) -> Unit
) : AnyTypeDelegate<List<EditorsChoice>, DelegateEditorsChoiceSectionBinding, Holder>() {
    private val delegate = EditorsChoiceDelegate(onItemClickListener)
    private val anyTypeAdapter = object : AnyTypeAdapter() {
        override fun getItemCount(): Int {
            return when {
                super.getItemCount() == 0 -> 0
                else -> Int.MAX_VALUE
            }
        }

        override fun onBindViewHolder(
            holder: AnyTypeViewHolder<Any, ViewBinding>,
            position: Int
        ) {
            val realPosition = position % anyTypeCollection.size
            super.onBindViewHolder(holder, realPosition)
        }

        override fun getItemViewType(position: Int): Int {
            val realPosition = position % anyTypeCollection.size
            return super.getItemViewType(realPosition)
        }
    }

    override fun createViewHolder(itemView: View): Holder = Holder(
        DelegateEditorsChoiceSectionBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_editors_choice_section

    override fun getItemId(item: List<EditorsChoice>): String = ITEM_ID

    inner class Holder(
        binding: DelegateEditorsChoiceSectionBinding
    ) : AnyTypeViewHolder<List<EditorsChoice>, DelegateEditorsChoiceSectionBinding>(binding) {
        private val recyclerView = binding.editorsChoiceSectionRv

        init {
            recyclerView.apply {
                adapter = anyTypeAdapter
                layoutManager = object : LinearLayoutManager(
                    context,
                    RecyclerView.HORIZONTAL,
                    false
                ) {
                    override fun supportsPredictiveItemAnimations(): Boolean = false
                }
                PagerSnapHelper().attachToRecyclerView(this)
            }
        }

        override fun bind(item: List<EditorsChoice>) {
            AnyTypeCollection.Builder()
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