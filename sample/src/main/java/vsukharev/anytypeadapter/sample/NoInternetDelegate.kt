package vsukharev.anytypeadapter.sample

import android.view.View
import kotlinx.android.synthetic.main.delegate_no_internet.view.*
import vsukharev.anytypeadapter.delegate.NoDataDelegate
import vsukharev.anytypeadapter.holder.NoDataViewHolder

/**
 * Delegate showing no internet state
 */
class NoInternetDelegate(private val onClickListener: (String) -> Unit) : NoDataDelegate() {

    override fun createViewHolder(itemView: View): NoDataViewHolder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_no_internet

    inner class Holder(itemView: View) : NoDataViewHolder(itemView) {
        init {
            with(itemView.albums_retry_btn) {
                setOnClickListener { onClickListener.invoke(text.toString()) }
            }
        }
    }
}