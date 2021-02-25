package vsukharev.anytypeadapter.sample.common.presentation.view

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.extension.commitShowAndHide
import vsukharev.anytypeadapter.sample.common.extension.doIfEmpty
import vsukharev.anytypeadapter.sample.common.extension.getFirstVisibleFragment
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedFragment
import vsukharev.anytypeadapter.sample.tracks.presentation.view.TracksFragment

@ExperimentalCoroutinesApi
internal class BottomViewTabManager(
    private val activity: AppCompatActivity,
    private val bottomView: BottomNavigationView
) {

    val initialSelectedTabId = R.id.feed
    var navigationPath = mutableListOf<Int>()

    internal inline fun addInitialFragment(fragmentCreationBlock: () -> Fragment) {
        with(activity) {
            supportFragmentManager.apply {
                val tag = initialSelectedTabId.toString()
                findFragmentByTag(tag) ?: run {
                    beginTransaction()
                        .add(R.id.main_container, fragmentCreationBlock.invoke(), tag)
                        .addToBackStack(tag)
                        .commit()
                }
                navigationPath.apply { doIfEmpty { add(initialSelectedTabId) } }
            }
        }
    }

    internal fun onBackPressed() {
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
                else -> activity.finish()
            }
        }
    }

    private fun goToPreviousTab() {
        with(activity.supportFragmentManager) {
            navigationPath.apply {
                val fragmentToShow = findFragmentByTag(last().toString())!!
                val fragmentToHide = getFirstVisibleFragment()!!
                beginTransaction()
                    .commitShowAndHide(fragmentToShow, fragmentToHide)
                bottomView.menu.findItem(last()).isChecked = true
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

    internal fun onMenuItemSelected(itemId: Int): Boolean {
        return with(activity.supportFragmentManager) {
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
            R.id.feed -> FeedFragment.newInstance()
            R.id.tracks -> TracksFragment.newInstance()
            else -> null
        }
    }
}