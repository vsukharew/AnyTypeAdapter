package vsukharev.anytypeadapter.sample.common.presentation

import moxy.MvpAppCompatFragment
import vsukharev.anytypeadapter.sample.common.presentation.view.ErrorHandlerView

abstract class BaseFragment : MvpAppCompatFragment(), ErrorHandlerView {
    private val errorHandlerView by lazy {
        (activity as? ErrorHandlerView)
    }

    override fun showNoInternetError(e: Throwable) {
        errorHandlerView?.showNoInternetError(e)
    }

    override fun hideError() {
        errorHandlerView?.hideError()
    }
}