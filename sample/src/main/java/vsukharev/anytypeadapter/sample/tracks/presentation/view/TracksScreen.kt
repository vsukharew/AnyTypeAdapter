package vsukharev.anytypeadapter.sample.tracks.presentation.view

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Abstraction describing the screen with tracks list
 */
class TracksScreen : SupportAppScreen() {
    override fun getFragment(): Fragment? {
        return TracksFragment.newInstance()
    }
}