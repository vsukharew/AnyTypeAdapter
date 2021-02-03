package vsukharev.anytypeadapter.sample

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import vsukharev.anytypeadapter.sample.common.presentation.view.BottomViewTabManager
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedFragment

private const val NAVIGATION_PATH_KEY = "navigation_path"

class MainActivity : BaseActivity() {

    private val tabManager by lazy {
        BottomViewTabManager(this, main_bottom_navigation_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_bottom_navigation_view.apply {
            setOnNavigationItemSelectedListener {
                if (selectedItemId != it.itemId) {
                    tabManager.onMenuItemSelected(it.itemId)
                } else {
                    false
                }
            }
        }

        tabManager.apply {
            navigationPath = savedInstanceState?.getIntArray(NAVIGATION_PATH_KEY)
                ?.toMutableList()
                ?: mutableListOf()
            addInitialFragment { FeedFragment.newInstance() }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntArray(NAVIGATION_PATH_KEY, tabManager.navigationPath.toIntArray())
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        tabManager.onBackPressed()
    }
}
