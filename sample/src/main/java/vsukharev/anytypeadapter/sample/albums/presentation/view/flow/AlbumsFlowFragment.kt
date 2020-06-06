package vsukharev.anytypeadapter.sample.albums.presentation.view.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.BaseFragment

/**
 * Container fragment fpr the "Albums" tab
 */
class AlbumsFlowFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    companion object {
        fun newInstance(): AlbumsFlowFragment = AlbumsFlowFragment()
    }
}