package vsukharev.anytypeadapter.sample.albums.presentation.view

import moxy.viewstate.strategy.alias.AddToEndSingle
import vsukharev.anytypeadapter.sample.albums.presentation.model.HomePageUi
import vsukharev.anytypeadapter.sample.common.presentation.view.BaseView

/**
 * The View of the albums screen
 */
interface AlbumsView : BaseView {
    @AddToEndSingle
    fun showData(data: HomePageUi)
}