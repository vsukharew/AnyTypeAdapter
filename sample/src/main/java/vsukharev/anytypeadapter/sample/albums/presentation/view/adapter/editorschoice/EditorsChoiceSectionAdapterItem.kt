package vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.editorschoice

import vsukharev.anytypeadapter.item.AdapterItem

/**
 * [AdapterItem] describes the whole "editor's choice" section
 */
class EditorsChoiceSectionAdapterItem(val adapterChoices: List<EditorsChoiceAdapterItem>) : AdapterItem {
    override fun areItemsTheSame(other: AdapterItem): Boolean = true

    override fun areContentsTheSame(other: AdapterItem): Boolean = true
}