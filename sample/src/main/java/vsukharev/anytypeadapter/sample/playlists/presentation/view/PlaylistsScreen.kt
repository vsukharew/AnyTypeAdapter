package vsukharev.anytypeadapter.sample.playlists.presentation.view

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Abstraction describing the screen with playlists
 */
class PlaylistsScreen : SupportAppScreen() {
    override fun getFragment(): Fragment? {
        return PlaylistsFragment.newInstance()
    }
}