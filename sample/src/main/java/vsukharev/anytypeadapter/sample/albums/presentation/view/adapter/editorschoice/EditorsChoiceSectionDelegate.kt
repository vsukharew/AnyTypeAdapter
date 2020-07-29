package vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.editorschoice

import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.editorschoice.EditorsChoiceSectionDelegate.Holder

/**
 * Delegate responsible for the whole "editor's choice" section
 */
class EditorsChoiceSectionDelegate : BaseDelegate<EditorsChoiceSectionAdapterItem, Holder>() {
    private val delegate = EditorsChoiceDelegate()

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_editors_choice_section

    inner class Holder(itemView: View) : BaseViewHolder<EditorsChoiceSectionAdapterItem>(itemView) {
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.editors_choice_section_rv)
        private val adapter = AnyTypeAdapter()

        init {
            recyclerView.adapter = adapter
            PagerSnapHelper().attachToRecyclerView(recyclerView)
        }

        override fun bind(item: EditorsChoiceSectionAdapterItem) {
            Collection.Builder()
                .add(item.adapterChoices, delegate)
                .build()
                .let { adapter.setItems(it) }
        }
    }
}