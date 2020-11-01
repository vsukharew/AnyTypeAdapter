package vsukharev.anytypeadapter.sample.feed.presentation.view

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class FeedScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return FeedFragment.newInstance()
    }
}