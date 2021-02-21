package vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter

import android.view.View
import kotlinx.android.synthetic.main.delegate_tracks_error.view.*
import vsukharev.anytypeadapter.delegate.NoDataDelegate
import vsukharev.anytypeadapter.holder.NoDataViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.databinding.DelegateTracksErrorBinding

class TracksErrorDelegate(
    private val retryClickListener: () -> Unit
) : NoDataDelegate<DelegateTracksErrorBinding>() {

    override fun createViewHolder(itemView: View): NoDataViewHolder<DelegateTracksErrorBinding> =
        Holder(DelegateTracksErrorBinding.bind(itemView))

    override fun getItemViewType(): Int = R.layout.delegate_tracks_error

    inner class Holder(binding: DelegateTracksErrorBinding) :
        NoDataViewHolder<DelegateTracksErrorBinding>(binding) {
        init {
            itemView.delegate_tracks_error_retry_btn.setOnClickListener {
                retryClickListener.invoke()
            }
        }
    }
}