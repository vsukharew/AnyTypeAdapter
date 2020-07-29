package vsukharev.anytypeadapter.sample.albums.presentation.view

import moxy.viewstate.strategy.alias.AddToEndSingle
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.AlbumsSectionAdapterItem
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.editorschoice.EditorsChoiceAdapterItem
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.editorschoice.EditorsChoiceSectionAdapterItem
import vsukharev.anytypeadapter.sample.common.presentation.view.BaseView

/**
 * The View of the albums screen
 */
interface AlbumsView : BaseView {
    @AddToEndSingle
    fun showItems(items: Pair<AlbumsSectionAdapterItem, EditorsChoiceSectionAdapterItem>)
}