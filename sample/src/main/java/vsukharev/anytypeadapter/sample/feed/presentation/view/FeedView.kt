package vsukharev.anytypeadapter.sample.feed.presentation.view

import moxy.viewstate.strategy.alias.AddToEndSingle
import vsukharev.anytypeadapter.sample.feed.presentation.model.FeedUi
import vsukharev.anytypeadapter.sample.common.presentation.view.ErrorHandlerView

/**
 * The View of the albums screen
 */
interface FeedView : ErrorHandlerView {
    @AddToEndSingle
    fun showData(data: FeedUi)

    @AddToEndSingle
    fun showProgress()

    @AddToEndSingle
    fun hideProgress()
}