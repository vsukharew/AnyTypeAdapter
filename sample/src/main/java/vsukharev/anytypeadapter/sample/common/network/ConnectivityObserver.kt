package vsukharev.anytypeadapter.sample.common.network

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Class for observing the network state
 */
class ConnectivityObserver(
    private val activity: Activity,
    private val onNetworkAvailable: (Network) -> Unit
) : LifecycleObserver {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            activity.runOnUiThread { onNetworkAvailable(network) }
        }
    }

    private val connectivityManager by lazy {
        activity.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun registerNetworkCallback() {
        connectivityManager?.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            networkCallback
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregisterNetworkCallback() {
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }
}