package vsukharev.anytypeadapter.sample.common.presentation.view

import moxy.viewstate.strategy.alias.AddToEndSingle

/**
 * View that is able to display errors
 */
interface ErrorHandlerView : BaseView {
    @AddToEndSingle
    fun showNoInternetError(e: Throwable)

    @AddToEndSingle
    fun showUnknownError(e: Throwable)

    @AddToEndSingle
    fun hideError()
}