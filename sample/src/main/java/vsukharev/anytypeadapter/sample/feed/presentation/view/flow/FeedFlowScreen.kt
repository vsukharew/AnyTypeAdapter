package vsukharev.anytypeadapter.sample.feed.presentation.view.flow

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * Abstraction describing the flow screen of the "Albums" tab
 */
class FeedFlowScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return FeedFlowFragment.newInstance()
    }
}