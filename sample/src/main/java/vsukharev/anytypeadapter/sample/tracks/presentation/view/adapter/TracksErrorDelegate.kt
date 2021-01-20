package vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter

import vsukharev.anytypeadapter.delegate.NoDataDelegate
import vsukharev.anytypeadapter.sample.R

class TracksErrorDelegate : NoDataDelegate() {
    override fun getItemViewType(): Int = R.layout.delegate_tracks_error
}