package vsukharev.anytypeadapter.sample

import android.view.View
import vsukharev.anytypeadapter.delegate.NoDataDelegate
import vsukharev.anytypeadapter.holder.NoDataViewHolder
import vsukharev.anytypeadapter.sample.databinding.DelegateNoInternetBinding

/**
 * Delegate showing no internet state
 */
class NoInternetDelegate(
    private val onClickListener: (String) -> Unit
) : NoDataDelegate<DelegateNoInternetBinding>() {

    override fun createViewHolder(itemView: View): NoDataViewHolder<DelegateNoInternetBinding> =
        Holder(DelegateNoInternetBinding.bind(itemView))

    override fun getItemViewType(): Int = R.layout.delegate_no_internet

    inner class Holder(
        binding: DelegateNoInternetBinding
    ) : NoDataViewHolder<DelegateNoInternetBinding>(binding) {
        init {
            with(binding.albumsRetryBtn) {
                setOnClickListener { onClickListener.invoke(text.toString()) }
            }
        }
    }
}