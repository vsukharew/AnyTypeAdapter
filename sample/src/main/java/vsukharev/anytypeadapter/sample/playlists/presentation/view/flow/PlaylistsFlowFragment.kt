package vsukharev.anytypeadapter.sample.playlists.presentation.view.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.BaseFragment

/**
 * Container fragment fpr the "Playlists" tab
 */
class PlaylistsFlowFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlists, container, false)
    }

    companion object {
        fun newInstance(): PlaylistsFlowFragment = PlaylistsFlowFragment()
    }
}