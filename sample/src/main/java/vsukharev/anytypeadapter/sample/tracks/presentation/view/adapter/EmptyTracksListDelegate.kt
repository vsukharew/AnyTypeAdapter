package vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter

import android.view.View
import vsukharev.anytypeadapter.delegate.NoDataDelegate
import vsukharev.anytypeadapter.holder.NoDataViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.databinding.DelegateEmptyTracksListBinding

class EmptyTracksListDelegate : NoDataDelegate<DelegateEmptyTracksListBinding>() {

    override fun createViewHolder(itemView: View) = NoDataViewHolder(
        DelegateEmptyTracksListBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_empty_tracks_list
}