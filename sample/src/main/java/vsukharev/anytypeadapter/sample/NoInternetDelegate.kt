package vsukharev.anytypeadapter.sample

import vsukharev.anytypeadapter.delegate.NoDataDelegate

/**
 * Delegate showing no internet state
 */
class NoInternetDelegate : NoDataDelegate() {
    override fun getItemViewType(): Int = R.layout.delegate_no_internet
}