package vsukharev.anytypeadapter.sample.albums.presentation.view

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AlbumsScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return AlbumsFragment.newInstance()
    }
}