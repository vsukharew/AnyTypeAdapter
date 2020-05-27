package vsukharev.anytypeadapter.sample.playlists.presentation.view.flow

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Abstraction describing the flow screen of the "Playlists" tab
 */
class PlaylistsFlowScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return PlaylistsFlowFragment.newInstance()
    }
}