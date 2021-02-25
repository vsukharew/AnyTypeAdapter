package vsukharev.anytypeadapter.sample.common.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import moxy.MvpAppCompatFragment
import vsukharev.anytypeadapter.sample.common.presentation.view.ErrorHandlerView

abstract class BaseFragment : MvpAppCompatFragment(), ErrorHandlerView {
    protected abstract val binding: ViewBinding

    private val errorHandlerView by lazy {
        (activity as? ErrorHandlerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun showNoInternetError(e: Throwable) {
        errorHandlerView?.showNoInternetError(e)
    }

    override fun hideError() {
        errorHandlerView?.hideError()
    }
}