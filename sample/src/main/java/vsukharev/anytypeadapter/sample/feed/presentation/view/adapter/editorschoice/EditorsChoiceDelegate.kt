package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice.EditorsChoiceDelegate.Holder

/**
 * Delegate responsible for the "editor's choice" cell creation
 */
class EditorsChoiceDelegate : BaseDelegate<EditorsChoice, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_editors_choice

    override fun getItemId(item: EditorsChoice): String = item.id.toString()

    class Holder(itemView: View) : BaseViewHolder<EditorsChoice>(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.editors_choice_cover_iv)
        private val performerTv: TextView = itemView.findViewById(R.id.editors_choice_performer_tv)
        private val descriptionTv: TextView = itemView.findViewById(R.id.editors_choice_description_tv)

        override fun bind(item: EditorsChoice) {
            with(item) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .into(imageView)
                performerTv.text = starName
                descriptionTv.text = description
            }
        }
    }
}
