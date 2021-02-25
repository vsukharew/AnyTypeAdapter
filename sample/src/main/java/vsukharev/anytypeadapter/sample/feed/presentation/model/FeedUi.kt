package vsukharev.anytypeadapter.sample.feed.presentation.model

import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.delegate.IconWithTextAdapterItem
import vsukharev.anytypeadapter.sample.feed.data.CHART_MENU_ITEM_ID
import vsukharev.anytypeadapter.sample.feed.data.RELEASES_MENU_ITEM_ID
import vsukharev.anytypeadapter.sample.feed.domain.model.Activity
import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice
import vsukharev.anytypeadapter.sample.feed.domain.model.MenuItem

/**
 * The model describing content at the feed tab
 */
data class FeedUi(
    private val menuItems: List<MenuItem>,
    val albums: List<Album>,
    val activities: List<Activity>,
    val editorsChoice: List<EditorsChoice>
) {
    val menuItemsUi = menuItems.map {
        IconWithTextAdapterItem(
            id = it.id,
            text = it.name,
            imageResId = when (it.id) {
                RELEASES_MENU_ITEM_ID -> R.drawable.ic_fresh_release
                CHART_MENU_ITEM_ID -> R.drawable.ic_chart
                else -> R.drawable.ic_mic
            }
        )
    }
}