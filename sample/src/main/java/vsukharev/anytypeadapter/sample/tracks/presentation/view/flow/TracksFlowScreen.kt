package vsukharev.anytypeadapter.sample.tracks.presentation.view.flow

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Abstraction describing the flow screen of the "Tracks" tab
 */
class TracksFlowScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return TracksFlowFragment.newInstance()
    }
}