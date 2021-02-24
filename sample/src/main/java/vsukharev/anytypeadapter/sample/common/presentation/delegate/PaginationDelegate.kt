package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import androidx.core.view.isVisible
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.databinding.DelegatePaginationBinding

class PaginationDelegate(
    private val onClickListener: () -> Unit
) : AnyTypeDelegate<PaginationAdapterItem, DelegatePaginationBinding, PaginationDelegate.Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(
        DelegatePaginationBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_pagination

    override fun getItemId(item: PaginationAdapterItem): String = item.id.toString()

    inner class Holder(
        binding: DelegatePaginationBinding
    ) : AnyTypeViewHolder<PaginationAdapterItem, DelegatePaginationBinding>(binding) {
        private val progressBar = binding.delegatePaginationPb
        private val retryBtn = binding.delegatePaginationRetryBtn

        override fun bind(item: PaginationAdapterItem) {
            with(item) {
                progressBar.isVisible = !isError
                retryBtn.apply {
                    isVisible = isError
                    setOnClickListener { onClickListener.invoke() }
                }
            }
        }
    }
}