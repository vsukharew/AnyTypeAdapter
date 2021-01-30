package vsukharev.anytypeadapter.sample.common.presentation.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import moxy.MvpPresenter
import vsukharev.anytypeadapter.sample.common.presentation.view.BaseView
import vsukharev.anytypeadapter.sample.common.presentation.view.ErrorHandlerView

open class BasePresenter<V : BaseView> : MvpPresenter<V>(), CoroutineScope by MainScope() {

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    protected fun showError(e: Throwable) {
        val view = viewState as? ErrorHandlerView
        view?.showNoInternetError(e)
    }
}