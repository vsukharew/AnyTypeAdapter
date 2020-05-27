package vsukharev.anytypeadapter.sample

import android.os.Bundle
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import vsukharev.anytypeadapter.sample.albums.presentation.view.flow.AlbumsFlowScreen
import vsukharev.anytypeadapter.sample.playlists.presentation.view.flow.PlaylistsFlowScreen
import vsukharev.anytypeadapter.sample.tracks.presentation.view.flow.TracksFlowScreen

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_bottom_navigation_view.apply {
            setOnNavigationItemSelectedListener { onMenuItemSelected(it.itemId) }
            val manager = supportFragmentManager
            val tag = R.id.albums.toString()
            manager.findFragmentByTag(tag) ?: run {
                val activeFragment = AlbumsFlowScreen().fragment
                manager.beginTransaction()
                    .add(activeFragment, tag)
                    .show(activeFragment)
                    .commit()
            }
        }
    }

    private fun onMenuItemSelected(itemId: Int): Boolean {
        return with(supportFragmentManager) {
            var selectedFragment = findFragmentByTag(itemId.toString())
            val transaction = beginTransaction()
            val visibleFragment = fragments.firstOrNull { !it.isHidden && it != selectedFragment }
            visibleFragment?.let { fragment ->
                selectedFragment?.let {
                    transaction.commitShowAndHide(it, fragment)
                    true
                } ?: run {
                    selectedFragment = getFragmentByMenuItemId(itemId)
                    selectedFragment?.let {
                        beginTransaction()
                            .add(it, itemId.toString())
                            .commitShowAndHide(it, fragment)
                        true
                    } ?: false
                }
            } ?: false
        }
    }

    private fun getFragmentByMenuItemId(itemId: Int): Fragment? {
        return when (itemId) {
            R.id.albums -> AlbumsFlowScreen().fragment
            R.id.playlists -> PlaylistsFlowScreen().fragment
            R.id.tracks -> TracksFlowScreen().fragment
            else -> null
        }
    }

    private fun FragmentTransaction.commitShowAndHide(
        fragmentToShow: Fragment,
        fragmentToHide: Fragment
    ): Int {
        return show(fragmentToShow)
            .hide(fragmentToHide)
            .commit()
    }
}
