package vsukharev.anytypeadapter.sample.common.presentation.delegate

import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.delegate_pagination.view.*
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R

class PaginationDelegate(
    private val onClickListener: () -> Unit
) : AnyTypeDelegate<PaginationAdapterItem, PaginationDelegate.Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_pagination

    override fun getItemId(item: PaginationAdapterItem): String = item.id.toString()

    inner class Holder(itemView: View) : AnyTypeViewHolder<PaginationAdapterItem>(itemView) {
        private val progressBar = itemView.delegate_pagination_pb
        private val retryBtn = itemView.delegate_pagination_retry_btn

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