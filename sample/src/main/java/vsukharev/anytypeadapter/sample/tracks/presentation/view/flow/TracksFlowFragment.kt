package vsukharev.anytypeadapter.sample.tracks.presentation.view.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.BaseFragment

/**
 * Container fragment fpr the "Tracks" tab
 */
class TracksFlowFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracks, container, false)
    }

    companion object {
        fun newInstance(): TracksFlowFragment = TracksFlowFragment()
    }
}