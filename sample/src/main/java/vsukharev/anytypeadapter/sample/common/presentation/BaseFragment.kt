package vsukharev.anytypeadapter.sample.common.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import moxy.MvpAppCompatFragment
import vsukharev.anytypeadapter.sample.common.presentation.view.ErrorHandlerView

abstract class BaseFragment : MvpAppCompatFragment(), ErrorHandlerView {
    private val errorHandlerView by lazy {
        (activity as? ErrorHandlerView)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        private var isReloaded = false

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if (!isReloaded) {
                activity?.runOnUiThread { onNetworkAvailable() }
                isReloaded = true
            }
        }
    }

    private val connectivityManager by lazy {
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

    override fun showNoInternetError(e: Throwable) {
        errorHandlerView?.showNoInternetError(e)
    }

    override fun showUnknownError(e: Throwable) {
        errorHandlerView?.showUnknownError(e)
    }

    override fun hideError() {
        errorHandlerView?.hideError()
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(networkCallback)
        } else {
            connectivityManager?.registerNetworkCallback(
                NetworkRequest.Builder().build(),
                networkCallback
            )
        }
    }

    override fun onStop() {
        super.onStop()
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    protected open fun onNetworkAvailable() {}
}