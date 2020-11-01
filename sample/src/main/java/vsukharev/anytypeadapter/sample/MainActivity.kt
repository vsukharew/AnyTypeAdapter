package vsukharev.anytypeadapter.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsFragment
import vsukharev.anytypeadapter.sample.albums.presentation.view.flow.AlbumsFlowScreen
import vsukharev.anytypeadapter.sample.common.extension.commitShowAndHide
import vsukharev.anytypeadapter.sample.common.extension.doIfEmpty
import vsukharev.anytypeadapter.sample.common.extension.getFirstVisibleFragment
import vsukharev.anytypeadapter.sample.playlists.presentation.view.flow.PlaylistsFlowScreen
import vsukharev.anytypeadapter.sample.tracks.presentation.view.flow.TracksFlowScreen

private const val NAVIGATION_PATH_KEY = "navigation_path"

class MainActivity : BaseActivity() {

    private val initialSelectedTabId = R.id.albums
    private var navigationPath = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationPath = savedInstanceState?.getIntArray(NAVIGATION_PATH_KEY)
            ?.toMutableList()
            ?: mutableListOf()

        main_bottom_navigation_view.apply {
            setOnNavigationItemSelectedListener {
                if (selectedItemId != it.itemId) {
                    onMenuItemSelected(it.itemId)
                } else {
                    false
                }
            }
        }
        supportFragmentManager.apply {
            val tag = initialSelectedTabId.toString()
            findFragmentByTag(tag) ?: run {
                val activeFragment = AlbumsFragment.newInstance()
                beginTransaction()
                    .add(R.id.main_container, activeFragment, tag)
                    .addToBackStack(tag)
                    .commit()
            }
            navigationPath.apply { doIfEmpty { add(initialSelectedTabId) } }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntArray(NAVIGATION_PATH_KEY, navigationPath.toIntArray())
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        with(navigationPath) {
            when {
                size > 1 -> {
                    removeAt(lastIndex)
                    goToPreviousTab()
                }
                size == 1 && first() != initialSelectedTabId -> {
                    putInitialSelectedItemToStart()
                    goToPreviousTab()
                    removeAt(lastIndex)
                }
                else -> finish()
            }
        }
    }

    private fun onMenuItemSelected(itemId: Int): Boolean {
        return with(supportFragmentManager) {
            val selectedFragment = findFragmentByTag(itemId.toString())
            val transaction = beginTransaction()
            val visibleFragment = getFirstVisibleFragment()
            visibleFragment?.let {
                selectedFragment?.let {
                    goToExistingScreen(transaction, it, visibleFragment, itemId)
                    true
                } ?: run {
                    getFragmentByMenuItemId(itemId)?.let {
                        goToNewScreen(it, visibleFragment, itemId)
                        true
                    } ?: false
                }
            } ?: false
        }
    }

    private fun FragmentManager.goToNewScreen(
        fragmentToShow: Fragment,
        fragmentToHide: Fragment,
        selectedItemId: Int
    ) {
        beginTransaction()
            .add(R.id.main_container, fragmentToShow, selectedItemId.toString())
            .commitShowAndHide(fragmentToShow, fragmentToHide)
        navigationPath.add(selectedItemId)
    }

    private fun goToExistingScreen(
        transaction: FragmentTransaction,
        fragmentToShow: Fragment,
        fragmentToHide: Fragment,
        selectedItemId: Int
    ) {
        transaction.commitShowAndHide(fragmentToShow, fragmentToHide)
        moveSelectedItemToEnd(selectedItemId)
    }

    private fun getFragmentByMenuItemId(itemId: Int): Fragment? {
        return when (itemId) {
            R.id.albums -> AlbumsFlowScreen().fragment
            R.id.playlists -> PlaylistsFlowScreen().fragment
            R.id.tracks -> TracksFlowScreen().fragment
            else -> null
        }
    }

    private fun goToPreviousTab() {
        with(supportFragmentManager) {
            navigationPath.apply {
                val fragmentToShow = findFragmentByTag(last().toString())!!
                val fragmentToHide = getFirstVisibleFragment()!!
                beginTransaction()
                    .commitShowAndHide(fragmentToShow, fragmentToHide)
                main_bottom_navigation_view.menu.findItem(last()).isChecked = true
            }
        }
    }

    private fun putInitialSelectedItemToStart() {
        navigationPath[0] = initialSelectedTabId
    }

    private fun moveSelectedItemToEnd(selectedItemId: Int) {
        navigationPath.apply {
            remove(selectedItemId)
            add(selectedItemId)
        }
    }
}
