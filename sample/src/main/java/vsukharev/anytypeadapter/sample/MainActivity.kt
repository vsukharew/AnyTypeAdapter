package vsukharev.anytypeadapter.sample

import android.os.Bundle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import vsukharev.anytypeadapter.sample.common.lifecycle.activityViewBinding
import vsukharev.anytypeadapter.sample.common.presentation.view.BottomViewTabManager
import vsukharev.anytypeadapter.sample.databinding.ActivityMainBinding
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedFragment

private const val NAVIGATION_PATH_KEY = "navigation_path"

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity() {

    private val binding:
            ActivityMainBinding by activityViewBinding(ActivityMainBinding::inflate)

    private val tabManager by lazy {
        BottomViewTabManager(this, binding.mainBottomNavigationView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            mainBottomNavigationView.apply {
                setOnNavigationItemSelectedListener {
                    if (selectedItemId != it.itemId) {
                        tabManager.onMenuItemSelected(it.itemId)
                    } else {
                        false
                    }
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