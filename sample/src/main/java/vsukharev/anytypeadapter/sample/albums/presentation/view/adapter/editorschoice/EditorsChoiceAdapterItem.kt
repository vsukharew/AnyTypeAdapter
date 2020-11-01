package vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.editorschoice

import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.sample.albums.domain.model.EditorsChoice

/**
 * The [AdapterItem] wrapping a single "editor's choice" track
 */
class EditorsChoiceAdapterItem(val choice: EditorsChoice) : AdapterItem {
    override fun areItemsTheSame(other: AdapterItem): Boolean =
        other is EditorsChoiceAdapterItem && choice.id == other.choice.id

    override fun areContentsTheSame(other: AdapterItem): Boolean =
        other is EditorsChoiceAdapterItem && choice == other.choice
}