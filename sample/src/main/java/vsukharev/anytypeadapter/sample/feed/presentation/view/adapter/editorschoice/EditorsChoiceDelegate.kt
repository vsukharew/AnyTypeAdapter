package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.databinding.DelegateEditorsChoiceBinding
import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice.EditorsChoiceDelegate.Holder

/**
 * Delegate responsible for the "editor's choice" cell creation
 */
class EditorsChoiceDelegate(
    private val onItemClickListener: (EditorsChoice) -> Unit
) : AnyTypeDelegate<EditorsChoice, DelegateEditorsChoiceBinding, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(
        DelegateEditorsChoiceBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_editors_choice

    override fun getItemId(item: EditorsChoice): String = item.id.toString()

    inner class Holder(
        binding: DelegateEditorsChoiceBinding
    ) : AnyTypeViewHolder<EditorsChoice, DelegateEditorsChoiceBinding>(binding) {
        private val imageView: ImageView = binding.editorsChoiceCoverIv
        private val performerTv: TextView = binding.editorsChoicePerformerTv
        private val descriptionTv: TextView = binding.editorsChoiceDescriptionTv

        override fun bind(item: EditorsChoice) {
            with(item) {
                Glide.with(context)
                    .load(imageUrl)
                    .into(imageView)
                imageView.setOnClickListener { onItemClickListener.invoke(this) }
                performerTv.text = starName
                descriptionTv.text = description
            }
        }
    }
}
