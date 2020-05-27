package vsukharev.anytypeadapter.sample.albums.presentation.view.flow

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Abstraction describing the flow screen of the "Albums" tab
 */
class AlbumsFlowScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return AlbumsFlowFragment.newInstance()
    }
}